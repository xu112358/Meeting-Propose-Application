package csci310.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import csci310.entity.Event;
import csci310.entity.Invite;
import csci310.entity.User;
import csci310.model.InviteModel;
import csci310.repository.EventRepository;
import csci310.repository.InviteRepository;
import csci310.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.sound.midi.Receiver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    InviteRepository inviteRepository;

    Map<String, List<Date>> userFailedAttempts = new HashMap<>();


    @GetMapping({"/","/signin"})
    public String index(){
        return "signin";
    }
    @PostMapping({"/","/signin"})

    public String signin(@RequestParam("username") String username, @RequestParam(name="password") String password, Model model, HttpSession session){
        User user=userRepository.findByUsername(username);
        List<Date> failedAttempts = userFailedAttempts.get(username);
        if(failedAttempts == null){
            failedAttempts = new ArrayList<>();
            userFailedAttempts.put(username,failedAttempts);
        }else if(failedAttempts.size() >= 3){
            Date failedTime = failedAttempts.get(failedAttempts.size() - 3);
            Date now = new Date();
            if(now.getTime() - failedTime.getTime() <= 60*1000){
                model.addAttribute("error_message", "Your account is locked!");
                return "signin";
            }
        }
        if ( user== null) {
            model.addAttribute("error_message", "Username does not exist!");
            model.addAttribute("username", username);
            model.addAttribute("password", password);
            return "signin";
        }
        else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if(encoder.matches(password,user.getHashPassword())){
                model.addAttribute("message","Login successfully");
                session.setAttribute("loginUser",username);
                userFailedAttempts.replace(username, new ArrayList<>());
                return "redirect:/home";
            }
            else{
                model.addAttribute("error_message", "Username and password do not match!");
                model.addAttribute("username", username);
                model.addAttribute("password", password);
                failedAttempts.add(new Date());
                userFailedAttempts.replace(username, failedAttempts);
                return "signin";
            }
        }

    }

    @GetMapping(value="/logout")
    public String logout(Model model,HttpSession session){
        session.removeAttribute("loginUser");
        return "redirect:/signin";

    }

    @RequestMapping(value="/signup", method = RequestMethod.GET)
    public String createSignupForm(Model model) {
        return "signup";
    }


    @PostMapping(value="/signup")
    public String createUser(@RequestParam("username") String username,@RequestParam(name="password") String password,@RequestParam(name="re_password",required = false) String re_password, Model model) throws Exception {

        User user=new User();
        user.setUsername(username);
        user.setHashPassword(password);

        if (userRepository.findByUsername(user.getUsername()) == null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(user.getHashPassword());
            user.setHashPassword(encodedPassword);
            try{
                userRepository.save(user);
            }catch(Exception e){

            }
            model.addAttribute("message", "Sign Up Successfully!");
            return "signup";
        }
        else {
            model.addAttribute("error_message", "Username is taken. Try another one.");
            return "signup";
        }
    }

    @PostMapping(value="/send-invite")
    public @ResponseBody Map<String, String> sendInvite(@RequestBody InviteModel inviteModel) {
        Map<String, String> responseMap = new HashMap<>();
        String senderUsername = inviteModel.getSender();
        List<String> receiversUsername = inviteModel.getReceivers();
        String inviteName = inviteModel.getInvite_name();
        User sender = userRepository.findByUsername(senderUsername);
        List<Event> events = inviteModel.getEvents();
        List<User> receivers = new ArrayList<>();
        for (String receiverUsername : receiversUsername) {
            receivers.add(userRepository.findByUsername(receiverUsername));
        }

        for (User receiver : receivers) {
            List<User> receiverBlockList = userRepository.findByUsername(receiver.getUsername()).getBlock_list();
            //Check if sender is on the block list of receiver
            for (User userOnBlockList : receiverBlockList) {
                if (sender.getId() == userOnBlockList.getId()) {
                    responseMap.put("message", "you are blocked by " + receiver.getUsername());
                    responseMap.put("returnCode", "400");
                    return responseMap;
                }
            }
            //Check if every receiver is able to attend every event
            for (Event event : events) {
                if (receiver.getStartDate() == null) { //we update startDate and endDate at the same time, check startDate would be enough
                    continue;
                }
                if (event.getEventDate().compareTo(receiver.getStartDate()) > 0 && event.getEventDate().compareTo(receiver.getEndDate()) < 0) {
                    responseMap.put("message", receiver.getUsername() + " can not attend " + event.getEventName());
                    responseMap.put("returnCode", "400");
                    return responseMap;
                }
            }
        }

        Invite invite = new Invite();
        invite.setInviteName(inviteName);
        Collections.sort(events, Comparator.comparing(Event::getEventDate));
        invite.setCreateDate(events.get(0).getEventDate());
        invite.setSender(sender);
        try{
            inviteRepository.save(invite);
        }catch (Exception e){

        }
        //can cause problem with multithreaded server
        invite = inviteRepository.findTopByOrderByIdDesc();
        for (int i = 0; i < receivers.size(); i++) {
            for (int j = 0; j < events.size(); j++) {
                Event event = new Event(events.get(j));
                event.setInvite(invite);
                event.setReceiver(receivers.get(i));
                try{
                    eventRepository.save(event);
                }catch (Exception e){

                }
            }
            //add received invite
            User receiver = receivers.get(i);
            List<Invite> newReceivedInviteList = receiver.getReceive_invites_list();
            newReceivedInviteList.add(invite);
            receiver.setReceive_invites_list(newReceivedInviteList);
            userRepository.save(receiver);
        }
        responseMap.put("message", "Invite Sent");
        responseMap.put("returnCode", "200");
        return responseMap;
    }

    @GetMapping(value="/find-received-invite")
    public String findUserInvite(Model model, HttpSession httpSession) {
        String username = (String)httpSession.getAttribute("loginUser");
        User receiver = userRepository.findByUsername(username);
        List<Event> userEvents = userRepository.findByUsername(username).getUser_events_list();
        List<Invite> invites = userRepository.findByUsername(username).getReceive_invites_list();
        List<Invite> invitesResult = new ArrayList<>();
        for(Invite invite : invites){
            //List<Event> inviteEvents = userEvents.stream().filter(userEvent -> userEvent.getInvites_which_hold_event().get(0).getId().equals(invite.getId())).collect(Collectors.toList());
            List<Event> inviteEvents = new ArrayList<>();
            for(Event userEvent : userEvents){
                // find overlap events of a received invite and receiver
                if(userEvent.getInvite().getId().equals(invite.getId())){
                    inviteEvents.add(userEvent);
                }
            }
            invite.setInvite_events_list(inviteEvents);
            invite.setSender(inviteRepository.findById(invite.getId()).get().getSender());
            invitesResult.add(invite);
        }
        model.addAttribute("invites", invitesResult);
        return "messages";
    }

    @GetMapping(value="/receive-groupDates")
    public String receiveGroupDate(Model model, HttpSession httpSession) {
        String cur_username=(String)httpSession.getAttribute("loginUser");
        User receiver=userRepository.findByUsername(cur_username);
        List<Invite> receive_invites=receiver.getReceive_invites_list();
        List<Invite> rejected_receive_invites=receiver.getReject_invites_list();
        List<Invite> confirm_receive_invites=receiver.getConfirmed_invites_list();

        List<Map<String,String>> list=new ArrayList<>();


        for(Invite obj:receive_invites){
            Map<String,String> map=new HashMap<>();
            map.put("inviteName",obj.getInviteName());
            map.put("inviteId",obj.getId().toString());
            map.put("date",obj.getCreateDate().toString());
            map.put("status","New");
            list.add(map);

        }

        for(Invite obj:rejected_receive_invites){
            Map<String,String> map=new HashMap<>();
            map.put("inviteName",obj.getInviteName());
            map.put("inviteId",obj.getId().toString());
            map.put("date",obj.getCreateDate().toString());
            map.put("status","Rejected");
            list.add(map);
        }

        for(Invite obj:confirm_receive_invites){
            Map<String,String> map=new HashMap<>();
            map.put("inviteName",obj.getInviteName());
            map.put("inviteId",obj.getId().toString());
            map.put("date",obj.getCreateDate().toString());
            map.put("status","Confirmed");
            list.add(map);
        }


        Collections.sort(list, new Comparator<Map<String, String>>() {
                    @Override
                    public int compare(Map<String, String> o1, Map<String, String> o2) {
                        return o1.get("date").compareTo(o2.get("date"));
                    }
                }
        );
        model.addAttribute("invites", list);

        return "recieive_invite";
    }
    @GetMapping(value = "/receive_invite_events")
    public String receiveGroupDateEvents(@RequestParam("inviteId") String inviteId,@RequestParam("status") String status,Model model, HttpSession httpSession) {
        String cur_username=(String)httpSession.getAttribute("loginUser");
        Long inviteId_value=Long.parseLong(inviteId);
        String invite_name="";
        Invite cur_invite=inviteRepository.getById(inviteId_value);

        User cur_user=userRepository.findByUsername(cur_username);
        List<Map<String,String>> list=new ArrayList<>();
        for(Event obj:cur_user.getUser_events_list()){
            if(obj.getInvite().getId().equals(inviteId_value)){
                Map<String,String> map=new HashMap<>();
                map.put("eventName",obj.getEventName());
                map.put("genre",obj.getGenre());
                map.put("location",obj.getLocation());
                map.put("date",obj.getEventDate().toString());
                map.put("sender",obj.getInvite().getSender().getUsername());
                map.put("preference",obj.getPreference()+"");
                map.put("availability",obj.getAvailability());
                map.put("eventId",obj.getId()+"");
                invite_name=obj.getInvite().getInviteName();
                list.add(map);
            }
        }
        List<Map<String,String>> other_user_list=new ArrayList<>();
        for(User temp:cur_invite.getReceivers()){
            if(temp.getUsername().equals(cur_username)){
                continue;
            }
            else{
                for(Event obj:temp.getUser_events_list()){
                    if(obj.getInvite().getId().equals(inviteId_value)){
                        Map<String,String> map=new HashMap<>();
                        map.put("Username", temp.getUsername());
                        map.put("eventName",obj.getEventName());
                        map.put("genre",obj.getGenre());
                        map.put("location",obj.getLocation());
                        map.put("date",obj.getEventDate().toString());
                        map.put("sender",obj.getInvite().getSender().getUsername());
                        map.put("preference",obj.getPreference()+"");
                        map.put("availability",obj.getAvailability());
                        map.put("eventId",obj.getId()+"");
                        other_user_list.add(map);
                    }
                }
            }
        }

        System.out.println(other_user_list);

        model.addAttribute("inviteId",inviteId);
        model.addAttribute("status",status);
        model.addAttribute("events",list);
        model.addAttribute("inviteName",invite_name);
        model.addAttribute("other_user_events",other_user_list);


        return "receive_invite_event";
    }

    @GetMapping(value = "/update_receive_invite_events")
    public @ResponseBody Map<String,String> updateReceiveGroupDateEvents(@RequestParam("eventId") Long eventId,@RequestParam("preference") int preference,@RequestParam("availability") String availability, HttpSession httpSession) {
        Map<String,String> result=new HashMap<>();
        String cur_username=(String)httpSession.getAttribute("loginUser");
        User cur_user=userRepository.findByUsername(cur_username);

        String message="Not Updated";
        for(Event obj: cur_user.getUser_events_list()){
            if(obj.getId().equals(eventId)){
                obj.setPreference(preference);
                obj.setAvailability(availability);
                message="Updated";

            }
        }
        userRepository.save(cur_user);
        result.put("message",message);

        return result;
    }

    @GetMapping(value = "/confirm_receive_invite")
    public String confirmReceiveGroupDate(@RequestParam("inviteId") Long inviteId, HttpSession httpSession) {
        String cur_username=(String)httpSession.getAttribute("loginUser");
        User cur_user=userRepository.findByUsername(cur_username);

        Invite find_invite=null;

        for(Invite obj:cur_user.getReceive_invites_list()){
            if(obj.getId().equals(inviteId)){
                find_invite=obj;

            }
        }
        if(find_invite!=null){
            cur_user.getConfirmed_invites_list().add(find_invite);
            cur_user.getReceive_invites_list().remove(find_invite);

            for(Event obj:cur_user.getUser_events_list()){
                if(obj.getInvite().getId().equals(inviteId)){
                    obj.setStatus("confirmed");
                }
            }
            userRepository.save(cur_user);
        }

        find_invite=null;

        for(Invite obj:cur_user.getReject_invites_list()){
            if(obj.getId().equals(inviteId)){
                find_invite=obj;

            }
        }
        if(find_invite!=null){
            cur_user.getReject_invites_list().remove(find_invite);
            cur_user.getConfirmed_invites_list().add(find_invite);

            for(Event obj:cur_user.getUser_events_list()){
                if(obj.getInvite().getId().equals(inviteId)){
                    obj.setStatus("confirmed");

                }
            }
            userRepository.save(cur_user);
        }

        Invite findInvite = inviteRepository.findById(inviteId).get();
        findInvite.setStatus("finalized responded");
        inviteRepository.save(findInvite);
        return "redirect:/receive-groupDates";
    }


    @GetMapping(value = "/reject_receive_invite")
    public String rejectReceiveGroupDate(@RequestParam("inviteId") Long inviteId, HttpSession httpSession) {
        String cur_username=(String)httpSession.getAttribute("loginUser");
        User cur_user=userRepository.findByUsername(cur_username);

        Invite find_invite=null;

        for(Invite obj:cur_user.getReceive_invites_list()){
            if(obj.getId().equals(inviteId)){
                find_invite=obj;
            }
        }
        if(find_invite!=null){
            cur_user.getReject_invites_list().add(find_invite);
            cur_user.getConfirmed_invites_list().remove(find_invite);

            for(Event obj:cur_user.getUser_events_list()){
                if(obj.getInvite().getId().equals(inviteId)){
                    obj.setStatus("not confirmed");
                    obj.setPreference(1);
                    obj.setAvailability("no");
                }
            }
            userRepository.save(cur_user);
        }


        find_invite=null;

        for(Invite obj:cur_user.getConfirmed_invites_list()){
            if(obj.getId().equals(inviteId)){
                find_invite=obj;

            }
        }
        if(find_invite!=null){
            cur_user.getReject_invites_list().add(find_invite);
            cur_user.getConfirmed_invites_list().remove(find_invite);

            for(Event obj:cur_user.getUser_events_list()){
                if(obj.getInvite().getId().equals(inviteId)){
                    obj.setStatus("not confirmed");
                    obj.setPreference(1);
                    obj.setAvailability("no");
                }
            }
            userRepository.save(cur_user);
        }
        Invite findInvite = inviteRepository.findById(inviteId).get();
        findInvite.setStatus("finalized responded");
        inviteRepository.save(findInvite);
        return "redirect:/receive-groupDates";
    }

    @GetMapping(value="/list-sent-invite")
    public String findSentInvite(@RequestParam(name = "type", required = false) String filter, Model model, HttpSession httpSession) {
        String username = (String)httpSession.getAttribute("loginUser");
        User user = userRepository.findByUsername(username);
        List<Invite> invites = user.getSend_invites_list();
        Collections.sort(invites, Comparator.comparing(Invite::getCreateDate));
//        if(filter != null){
//            if(filter.equals("finalized")){
//                List<Invite> tmp = new ArrayList<>();
//                for(Invite invite : invites) {
//                    if (invite.getStatus().equals("finalized responded") || invite.getStatus().equals("finalized not responded")) {
//                        tmp.add(invite);
//                    }
//                }
//                invites = tmp;
//            }else if(filter.equals("not finalized")){
//                List<Invite> tmp = new ArrayList<>();
//                for(Invite invite : invites) {
//                    System.out.println(invite.getStatus());
//                    if (invite.getStatus().equals("not finalized")) {
//                        tmp.add(invite);
//                    }
//                }
//                invites = tmp;
//            }else if(filter.equals("responded")){
//                List<Invite> tmp = new ArrayList<>();
//                for(Invite invite : invites) {
//                    if (invite.getStatus().equals("finalized responded")) {
//                        tmp.add(invite);
//                    }
//                }
//                invites = tmp;
//            }else if(filter.equals("not responded")){
//                List<Invite> tmp = new ArrayList<>();
//                for(Invite invite : invites) {
//                    if (invite.getStatus().equals("finalized not responded")) {
//                        tmp.add(invite);
//                    }
//                }
//                invites = tmp;
//            }
//        }
        model.addAttribute("invites", invites);
        return "sent_groupDate";
    }

    @GetMapping(value="/list-sent-invite-event")
    public String findSentInviteEvent(@RequestParam("inviteId") Long inviteId, Model model, HttpSession httpSession) {
        Invite invite = inviteRepository.findById(inviteId).get();
        List<Event> events = inviteRepository.findById(inviteId).get().getInvite_events_list();
        Map<String, List<Event>> eventsMap = new HashMap<>();
        List<Event> eventMap = new ArrayList<>();
        for(Event event : events){
            String eventKey = event.getEventName() + event.getEventDate().toString();
            List<Event> tmp = eventsMap.get(eventKey);
            if(tmp == null){
                tmp = new ArrayList<>();
            }
            event.setReceiver(eventRepository.findById(event.getId()).get().getReceiver());
            tmp.add(event);
            eventsMap.put(eventKey, tmp);
        }
        Map<Event, String> eventsList = new HashMap<>();
        for(Map.Entry<String, List<Event>> eventsMapEntry : eventsMap.entrySet()){
            for(Event EachEvent : eventsMapEntry.getValue()){
                eventsList.put(EachEvent ,EachEvent.getReceiver().getUsername());
            }
        }
        for(Map.Entry<String, List<Event>> eventsMapEntry : eventsMap.entrySet()){
            eventMap.add(eventsMapEntry.getValue().get(0));
        }

        Map<User, String> receiversMap = new HashMap<>();
        List<User> receivers = inviteRepository.getById(inviteId).getReceivers();
        for(User receiver : receivers){
            receiversMap.put(receiver, "undecided");
        }

        receivers = inviteRepository.getById(inviteId).getConfirmed_receivers();

        for(User receiver : receivers){
            receiversMap.put(receiver, "confirmed");
        }

        receivers = inviteRepository.getById(inviteId).getReject_receivers();
        for(User receiver : receivers){
            receiversMap.put(receiver, "rejected");
        }

        model.addAttribute("eventsReceivers", eventsList);
        model.addAttribute("receivers", receiversMap);
        model.addAttribute("events", eventMap);
        model.addAttribute("invite", invite);
        return "sent_invite_event";
    }

    @GetMapping(value="set-finalized-event")
    public String setFinalizedEvent(@RequestParam("inviteId") Long inviteId, @RequestParam("eventId") Long eventId, Model model, HttpSession httpSession) {
        Invite invite = inviteRepository.findById(inviteId).get();
        Event event = eventRepository.findById(eventId).get();
        invite.setFinalEvent(event);
        invite.setStatus("finalized not responded");
        inviteRepository.save(invite);


        return "redirect:/list-sent-invite-event?inviteId=" + inviteId;
    }

    @GetMapping(value="/delete-sent-invite")
    public String deleteSentInvite(@RequestParam("inviteId") Long inviteId, Model model, HttpSession httpSession){
        Invite invite = inviteRepository.findById(inviteId).get();
        List<Event> events = invite.getInvite_events_list();
        invite.setInvite_events_list(new ArrayList<>());
        inviteRepository.save(invite);
        for(Event event : events){
            eventRepository.delete(event);
        }
        invite.setReceivers(null);
        invite.setSender(null);
        invite.setConfirmed_receivers(null);
        invite.setReject_receivers(null);
        inviteRepository.save(invite);
        inviteRepository.delete(invite);
        return "redirect:/list-sent-invite-event?inviteId=" + inviteId;
    }

    @GetMapping(value="/delete-sent-invite-event")
    public String deleteSentInviteEvent(@RequestParam("inviteId") Long inviteId, @RequestParam("eventId") Long eventId, Model model, HttpSession httpSession) {
        Invite invite = inviteRepository.findById(inviteId).get();
        List<Event> events = inviteRepository.findById(inviteId).get().getInvite_events_list();
        Event event = eventRepository.getById(eventId);
        String eventAndDate = event.getEventName() + event.getEventDate().toString();
        List<Event> tmp = new ArrayList<>();
        for(Event eachEvent : events){
            if(!eventAndDate.equals(eachEvent.getEventName() + eachEvent.getEventDate().toString())){
                tmp.add(eachEvent);
            }
        }
        invite.setInvite_events_list(tmp);
        inviteRepository.save(invite);
        for(Event eachEvent : events){
            if(eventAndDate.equals(eachEvent.getEventName() + eachEvent.getEventDate().toString())){
                eventRepository.delete(eachEvent);
            }
        }
        if(invite.getInvite_events_list().size() == 0){
            invite.setReceivers(null);
            invite.setSender(null);
            invite.setConfirmed_receivers(null);
            invite.setReject_receivers(null);
            inviteRepository.save(invite);
            inviteRepository.delete(invite);
            return "redirect:/list-sent-invite";
        }
        model.addAttribute("message", "invite changed");
        return "redirect:/list-sent-invite-event?inviteId=" + inviteId;
    }

    @GetMapping(value="/delete-sent-invite-user")
    public String deleteSentInviteUser(@RequestParam("inviteId") Long inviteId, @RequestParam("username") String username, Model model, HttpSession httpSession) {
        Invite invite = inviteRepository.findById(inviteId).get();
        long targetId = invite.getId();
        User receiver = userRepository.findByUsername(username);
        List<Invite> receivedInviteList = userRepository.findByUsername(username).getReceive_invites_list();
        List<Event> receivedEventList = userRepository.findByUsername(username).getUser_events_list();
        //receivedInviteList.removeIf(Eachinvite -> Eachinvite.getId() == targetId);
        for(int i = 0; i < receivedInviteList.size();){
            if(receivedInviteList.get(i).getId() == targetId){
                receivedInviteList.remove(i);
            }else{
                i ++;
            }
        }
        //receivedEventList.removeIf(EachEvent -> EachEvent.getInvite().getId() == targetId);
        for(int i = 0; i < receivedEventList.size();){
            if(receivedEventList.get(i).getInvite().getId() == targetId){
                receivedEventList.remove(i);
            }else{
                i ++;
            }
        }
        receiver.setReceive_invites_list(receivedInviteList);
        receiver.setUser_events_list(receivedEventList);
        userRepository.save(receiver);
        if(invite.getReceivers().size() == 0){
            //invite.setReceivers(null);
            invite.setSender(null);
            invite.setConfirmed_receivers(null);
            invite.setReject_receivers(null);
            inviteRepository.save(invite);
            inviteRepository.delete(invite);
            return "redirect:/list-sent-invite";
        }
        model.addAttribute("message", "invite changed");
        return "redirect:/list-sent-invite-event?inviteId=" + inviteId;
    }

    @GetMapping(value="/search-event-by-invite-and-username")
    public @ResponseBody List<Event> eventSearch(@RequestParam("username") String username, @RequestParam("invite_id") Long inviteId) {
        List<Event> userEvents = userRepository.findByUsername(username).getUser_events_list();
        List<Event> inviteEvents = inviteRepository.findById(inviteId).get().getInvite_events_list();
        List<Event> useInviteEvent = new ArrayList<>();
        for(Event userEvent : userEvents){
//             Event event = inviteEvents.stream().filter(inviteEvent -> userEvent.getId().equals(inviteEvent.getId())).findAny().orElse(null);
//             if(event != null){
//                 useInviteEvent.add(event);
//             }
            for(Event inviteEvent : inviteEvents){
                if(userEvent.getId().equals(inviteEvent.getId())){
                    useInviteEvent.add(userEvent);
                }
            }
        }
        return useInviteEvent;
    }

    //this api has not been completed
    @PostMapping(value="/propose-finalize-invite")
    /*          status, preference, availablilty should be ignored in the return json
     * {"average":4.0,"median":3.0,"proposedEvent":{"id":8,"eventName":"Justin Bieber","genre":"Music","eventDate":"2021-10-15","location":"Los Angeles","status":"confirmed","preference":5,"availability":"yes"}}
     * */
    public @ResponseBody Map<String, Object> finalizeInvite(@RequestParam("invite_id") Long inviteId) {
        Optional<Invite> inviteOptional = inviteRepository.findById(inviteId);
        Map<String, Object> response = new HashMap<>();
        //get invite
        List<Event> events = inviteOptional.get().getInvite_events_list();
        //event name + event date map to same events with different users
        Map<String, List<Event>> eventMap = new HashMap<>();
        for(Event event :events) {
            //do not need to check if event is not confirmed or rejected because they give 1 to all events
            String eventKey = event.getEventName() + event.getEventDate().toString();
            List<Event> tmp = eventMap.get(eventKey);
            if(tmp == null){
                tmp = new ArrayList<>();
            }
            tmp.add(event);
            eventMap.put(eventKey, tmp);
        }
        Map<String, Double> eventEvaluation = new HashMap<>();
        // Iterating eventMap through for loop
        for (Map.Entry<String, List<Event>> eventKeyValue : eventMap.entrySet()) {
            //evaluate event feasibility
            double evaluate = 0;
            for(Event event : eventKeyValue.getValue()){
                double multiply = 0;
                if(event.getAvailability().equals("yes")){
                    multiply = 1;
                }else if(event.getAvailability().equals("maybe")){
                    multiply = 0.5;
                }else{ // availability: no ---- don't change it cuz branch coverage
                    multiply = 0;
                }
                evaluate += multiply * event.getPreference();
            }
            eventEvaluation.put(eventKeyValue.getKey(), evaluate);
        }
        //find event with highest evaluation
        List<Map.Entry<String, Double>> eventKeyValues = new ArrayList<>(eventEvaluation.entrySet());
        eventKeyValues.sort(Map.Entry.comparingByValue());
        Map.Entry<String, Double> maxPair = eventKeyValues.get(eventKeyValues.size() - 1);
        Double median = 0d;
        Double sum = 0d;
        Double average;
        if(eventKeyValues.size() % 2 == 0){
            Double left = eventKeyValues.get(eventKeyValues.size() / 2 - 1).getValue();
            Double right = eventKeyValues.get(eventKeyValues.size() / 2).getValue();
            median = (left + right) / 2;
        }else{
            median = eventKeyValues.get(eventKeyValues.size() / 2).getValue();
        }
        for(Map.Entry<String, Double> eventKeyValue : eventKeyValues){
            sum += eventKeyValue.getValue();
        }
        average = sum / eventKeyValues.size();
        //procedure after finalize invite
        Event finalEvent = eventMap.get(maxPair.getKey()).get(0);
        response.put("proposedEvent", finalEvent);
        response.put("average", average);
        response.put("median", median);
        return response;
    }

    @PostMapping(value="/add-blocked-user")
    public @ResponseBody Map<String, String> addBlockedUser(Model model, @RequestParam("username") String username, @RequestParam("block") String block) {
        User user = userRepository.findByUsername(username);
        User toBlock = userRepository.findByUsername(block);
        Map<String, String> response = new HashMap<>();

        List<User> blockedUsers = userRepository.findByUsername(username).getBlock_list();
        User isBlocked = null;
        for(User blockeduser : blockedUsers){
            if(blockeduser.getId() == toBlock.getId()){
                isBlocked = blockeduser;
            }
        }
        if(isBlocked != null){
            response.put("message", block + " is already on your blocked list");
            response.put("returnCode", "400");
            return response;
        }
        user.getBlock_list().add(toBlock);
        userRepository.save(user);
        response.put("message", "added to blocklist");
        response.put("returnCode", "200");
        return response;
    }

    @GetMapping(value="/get-blocked-user")
    public @ResponseBody Map<String, List<String>> getBlockedUser(@RequestParam("username") String username) {
        List<User> users = userRepository.findByUsername(username).getBlock_list();
        List<String> usernameList = new ArrayList<>();
        for(User user : users){
            usernameList.add(user.getUsername());
        }
        Map<String, List<String>> result = new HashMap<>();
        result.put("blocked_usernames",usernameList);
        return result;

    }

    @PostMapping(value="/delete-blocked-user")
    public @ResponseBody Map<String, String> deleteBlockedUser(@RequestParam("blocked") String blockedUsername, HttpSession httpSession) {
        Map<String, String> response = new HashMap<>();
        String username = (String)httpSession.getAttribute("loginUser");
        User user = userRepository.findByUsername(username);
        User toBlock = userRepository.findByUsername(blockedUsername);
        List<User> users = userRepository.findByUsername(username).getBlock_list();
        users.remove(toBlock);
        user.setBlock_list(users);
        userRepository.save(user);
        response.put("message", "User unblocked");
        response.put("returnCode", "200");
        return response;
    }

    @PostMapping(value = "/usernameStartingWith", consumes = "application/json")
    @ResponseBody
    public Map<String,List<String>> usernameStartingWith(@RequestBody Map<String,Object> map,HttpSession session) throws JsonProcessingException {
        String name=(String) map.get("name");
        //List<User> users=userRepository.findByUsernameStartingWith(name);
        List<User> users = userRepository.findAll();
        List<User> tmp = new ArrayList<>();
        int nameLen = name.length();
        for(User user : users){
            if(user.getUsername().length()<nameLen){
                continue;
            }

            if(name.equals(user.getUsername().substring(0, nameLen))){
                tmp.add(user);
            }
        }
        users = tmp;

        String cur_username=(String)session.getAttribute("loginUser");

        List<String> usernames=new ArrayList<>();

        for(User obj:users){

            if(cur_username.equals(obj.getUsername())){
                continue;
            }
            String sb="";
            boolean found=false;
            for(User block_user:obj.getBlock_list()){
                if(cur_username.equals(block_user.getUsername())){

                    found=true;

                }
            }
            sb+=obj.getUsername();
            if(found){
                sb+="/ blocked you";


            }

            if(obj.getStartDate()!=null){
                sb+="/  No time from "+obj.getStartDate().toString()+" to "+obj.getEndDate().toString();
            }
            usernames.add(sb);

        }

        Map<String, List<String>> result = new HashMap<>();

        result.put("names",usernames);
        return result;

    }

    @PostMapping(value="/reply-invite")
    public @ResponseBody Map<String, String> replyInvite (@RequestBody Invite invite) throws JsonProcessingException {
        List<Event> events = invite.getInvite_events_list();
        Map<String, String> response = new HashMap<>();
        for(Event event : events){
            Event responseEvent = eventRepository.getById(event.getId());
            responseEvent.setStatus("confirmed");
            responseEvent.setPreference(event.getPreference());
            responseEvent.setAvailability(event.getAvailability());
            eventRepository.save(responseEvent);
        }
        response.put("message", "Reply Sent");
        response.put("returnCode", "200");
        return response;
    }


    @GetMapping(value="/update-unavailable-date")
    public String updateUserDateRange (HttpSession session, @RequestParam("startDate") String start, @RequestParam("endDate") String end) throws Exception {
        System.out.println("update-unavailable-date");
        String cur_usrname=(String)session.getAttribute("loginUser");
        User user = userRepository.findByUsername(cur_usrname);

        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(end);
        user.setStartDate(startDate);
        user.setEndDate(endDate);
        userRepository.save(user);
        return "redirect:/setting";
    }

    @GetMapping(value="/remove-unavailable-date")
    public String removeUserDateRange (HttpSession session) throws Exception {
        String cur_usrname=(String)session.getAttribute("loginUser");
        User user = userRepository.findByUsername(cur_usrname);

        user.setStartDate(null);
        user.setEndDate(null);
        userRepository.save(user);
        return "redirect:/setting";
    }

    @GetMapping(value="/setting")
    public String setting(HttpSession httpSession, Model model){
        String name=(String)httpSession.getAttribute("loginUser");
        User user=userRepository.findByUsername(name);


        if(user.getStartDate()==null){
            model.addAttribute("startDate","null");
            model.addAttribute("endDate","null");
        }
        else{
            model.addAttribute("startDate",user.getStartDate().toString());
            model.addAttribute("endDate",user.getEndDate().toString());
        }

        return "setting";
    }

    @GetMapping(value="/search-ticketmaster-event")
    /*
     * sample event return
     * {"_embedded":{"events":[{"name":"Justin Bieber","type":"event","id":"Z7r9jZ1AdAfja","test":false,"url":"https://ticketmaster.evyy.net/c/123/264167/4272?u=https%3A%2F%2Fwww.ticketmaster.com%2Fevent%2FZ7r9jZ1AdAfja","locale":"en-us","images":[{"ratio":"4_3","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_CUSTOM.jpg","width":305,"height":225,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_LANDSCAPE_16_9.jpg","width":1136,"height":639,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RECOMENDATION_16_9.jpg","width":100,"height":56,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_3_2.jpg","width":1024,"height":683,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_ARTIST_PAGE_3_2.jpg","width":305,"height":203,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_PORTRAIT_16_9.jpg","width":640,"height":360,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_16_9.jpg","width":1024,"height":576,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_PORTRAIT_3_2.jpg","width":640,"height":427,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_EVENT_DETAIL_PAGE_16_9.jpg","width":205,"height":115,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_LARGE_16_9.jpg","width":2048,"height":1152,"fallback":false}],"sales":{"public":{"startDateTime":"1900-01-01T06:00:00Z","startTBD":false,"startTBA":false,"endDateTime":"2022-03-08T03:30:00Z"}},"dates":{"start":{"localDate":"2022-03-07","localTime":"19:30:00","dateTime":"2022-03-08T03:30:00Z","dateTBD":false,"dateTBA":false,"timeTBA":false,"noSpecificTime":false},"status":{"code":"rescheduled"},"spanMultipleDays":false},"classifications":[{"primary":true,"segment":{"id":"KZFzniwnSyZfZ7v7nJ","name":"Music"},"genre":{"id":"KnvZfZ7vAev","name":"Pop"},"subGenre":{"id":"KZazBEonSMnZfZ7vkEl","name":"Pop Rock"},"family":false}],"outlets":[{"url":"https://www.staplescenter.com","type":"venueBoxOffice"},{"url":"https://www.ticketmaster.com/justin-bieber-los-angeles-california-03-07-2022/event/Zu0FM1R0e5t5lRJ","type":"tmMarketPlace"}],"seatmap":{"staticUrl":"http://resale.ticketmaster.com.au/akamai-content/maps/1604-20510-2-main.gif"},"_links":{"self":{"href":"/discovery/v2/events/Z7r9jZ1AdAfja?locale=en-us"},"attractions":[{"href":"/discovery/v2/attractions/K8vZ917G9e7?locale=en-us"}],"venues":[{"href":"/discovery/v2/venues/ZFr9jZe6vA?locale=en-us"}]},"_embedded":{"venues":[{"name":"STAPLES Center","type":"venue","id":"ZFr9jZe6vA","test":false,"locale":"en-us","postalCode":"90017","timezone":"America/Los_Angeles","city":{"name":"Los Angeles"},"state":{"name":"California","stateCode":"CA"},"country":{"name":"United States Of America","countryCode":"US"},"address":{"line1":"1111 S. Figueroa St."},"location":{"longitude":"-118.2649","latitude":"34.053101"},"dmas":[{"id":324}],"upcomingEvents":{"_total":139,"tmr":100,"ticketmaster":39},"_links":{"self":{"href":"/discovery/v2/venues/ZFr9jZe6vA?locale=en-us"}}}],"attractions":[{"name":"Justin Bieber","type":"attraction","id":"K8vZ917G9e7","test":false,"url":"https://www.ticketmaster.com/justin-bieber-tickets/artist/1369961","locale":"en-us","externalLinks":{"youtube":[{"url":"https://www.youtube.com/user/JustinBieberVEVO"},{"url":"https://www.youtube.com/user/kidrauhl"}],"twitter":[{"url":"https://twitter.com/justinbieber"}],"itunes":[{"url":"https://itunes.apple.com/artist/id320569549"}],"lastfm":[{"url":"http://www.last.fm/music/Justin+Bieber"}],"facebook":[{"url":"https://www.facebook.com/JustinBieber"}],"wiki":[{"url":"https://en.wikipedia.org/wiki/Justin_Bieber"}],"spotify":[{"url":"https://open.spotify.com/artist/1uNFoZAHBGtllmzznpCI3s"}],"instagram":[{"url":"http://instagram.com/justinbieber"}],"musicbrainz":[{"id":"e0140a67-e4d1-4f13-8a01-364355bee46e"}],"homepage":[{"url":"http://www.justinbiebermusic.com/"}]},"aliases":["justin birb","justin bueb","justin bue","justin biev","justin bib","justin biber"],"images":[{"ratio":"4_3","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_CUSTOM.jpg","width":305,"height":225,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_LANDSCAPE_16_9.jpg","width":1136,"height":639,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RECOMENDATION_16_9.jpg","width":100,"height":56,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_3_2.jpg","width":1024,"height":683,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_ARTIST_PAGE_3_2.jpg","width":305,"height":203,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_PORTRAIT_16_9.jpg","width":640,"height":360,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_16_9.jpg","width":1024,"height":576,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_PORTRAIT_3_2.jpg","width":640,"height":427,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_EVENT_DETAIL_PAGE_16_9.jpg","width":205,"height":115,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_LARGE_16_9.jpg","width":2048,"height":1152,"fallback":false}],"classifications":[{"primary":true,"segment":{"id":"KZFzniwnSyZfZ7v7nJ","name":"Music"},"genre":{"id":"KnvZfZ7vAeA","name":"Rock"},"subGenre":{"id":"KZazBEonSMnZfZ7v6F1","name":"Pop"},"type":{"id":"KZAyXgnZfZ7v7nI","name":"Undefined"},"subType":{"id":"KZFzBErXgnZfZ7v7lJ","name":"Undefined"},"family":false}],"upcomingEvents":{"_total":77,"tmr":10,"mfx-za":2,"mfx-be":1,"mfx-dk":2,"mfx-nl":1,"ticketmaster":58,"mfx-cz":1,"mfx-ch":1,"mfx-pl":1},"_links":{"self":{"href":"/discovery/v2/attractions/K8vZ917G9e7?locale=en-us"}}}]}},{"name":"Justin Bieber","type":"event","id":"Z7r9jZ1AdAfjp","test":false,"url":"https://ticketmaster.evyy.net/c/123/264167/4272?u=http%3A%2F%2Fwww.ticketsnow.com%2FInventoryBrowse%2FTicketList.aspx%3FPID%3D3154716","locale":"en-us","images":[{"ratio":"4_3","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_CUSTOM.jpg","width":305,"height":225,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_LANDSCAPE_16_9.jpg","width":1136,"height":639,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RECOMENDATION_16_9.jpg","width":100,"height":56,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_3_2.jpg","width":1024,"height":683,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_ARTIST_PAGE_3_2.jpg","width":305,"height":203,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_PORTRAIT_16_9.jpg","width":640,"height":360,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_16_9.jpg","width":1024,"height":576,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_PORTRAIT_3_2.jpg","width":640,"height":427,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_EVENT_DETAIL_PAGE_16_9.jpg","width":205,"height":115,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_LARGE_16_9.jpg","width":2048,"height":1152,"fallback":false}],"sales":{"public":{"startDateTime":"1900-01-01T06:00:00Z","startTBD":false,"startTBA":false,"endDateTime":"2022-03-09T03:30:00Z"}},"dates":{"start":{"localDate":"2022-03-08","localTime":"19:30:00","dateTime":"2022-03-09T03:30:00Z","dateTBD":false,"dateTBA":false,"timeTBA":false,"noSpecificTime":false},"status":{"code":"rescheduled"},"spanMultipleDays":false},"classifications":[{"primary":true,"segment":{"id":"KZFzniwnSyZfZ7v7nJ","name":"Music"},"genre":{"id":"KnvZfZ7vAev","name":"Pop"},"subGenre":{"id":"KZazBEonSMnZfZ7vkEl","name":"Pop Rock"},"family":false}],"outlets":[{"url":"https://www.staplescenter.com","type":"venueBoxOffice"},{"url":"https://www.ticketmaster.com/justin-bieber-los-angeles-california-03-08-2022/event/Zu0FM1R0e5t5lRg","type":"tmMarketPlace"}],"seatmap":{"staticUrl":"http://resale.ticketmaster.com.au/akamai-content/maps/1604-1-2-main.gif"},"_links":{"self":{"href":"/discovery/v2/events/Z7r9jZ1AdAfjp?locale=en-us"},"attractions":[{"href":"/discovery/v2/attractions/K8vZ917G9e7?locale=en-us"}],"venues":[{"href":"/discovery/v2/venues/ZFr9jZe6vA?locale=en-us"}]},"_embedded":{"venues":[{"name":"STAPLES Center","type":"venue","id":"ZFr9jZe6vA","test":false,"locale":"en-us","postalCode":"90017","timezone":"America/Los_Angeles","city":{"name":"Los Angeles"},"state":{"name":"California","stateCode":"CA"},"country":{"name":"United States Of America","countryCode":"US"},"address":{"line1":"1111 S. Figueroa St."},"location":{"longitude":"-118.2649","latitude":"34.053101"},"dmas":[{"id":324}],"upcomingEvents":{"_total":139,"tmr":100,"ticketmaster":39},"_links":{"self":{"href":"/discovery/v2/venues/ZFr9jZe6vA?locale=en-us"}}}],"attractions":[{"name":"Justin Bieber","type":"attraction","id":"K8vZ917G9e7","test":false,"url":"https://www.ticketmaster.com/justin-bieber-tickets/artist/1369961","locale":"en-us","externalLinks":{"youtube":[{"url":"https://www.youtube.com/user/JustinBieberVEVO"},{"url":"https://www.youtube.com/user/kidrauhl"}],"twitter":[{"url":"https://twitter.com/justinbieber"}],"itunes":[{"url":"https://itunes.apple.com/artist/id320569549"}],"lastfm":[{"url":"http://www.last.fm/music/Justin+Bieber"}],"facebook":[{"url":"https://www.facebook.com/JustinBieber"}],"wiki":[{"url":"https://en.wikipedia.org/wiki/Justin_Bieber"}],"spotify":[{"url":"https://open.spotify.com/artist/1uNFoZAHBGtllmzznpCI3s"}],"instagram":[{"url":"http://instagram.com/justinbieber"}],"musicbrainz":[{"id":"e0140a67-e4d1-4f13-8a01-364355bee46e"}],"homepage":[{"url":"http://www.justinbiebermusic.com/"}]},"aliases":["justin birb","justin bueb","justin bue","justin biev","justin bib","justin biber"],"images":[{"ratio":"4_3","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_CUSTOM.jpg","width":305,"height":225,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_LANDSCAPE_16_9.jpg","width":1136,"height":639,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RECOMENDATION_16_9.jpg","width":100,"height":56,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_3_2.jpg","width":1024,"height":683,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_ARTIST_PAGE_3_2.jpg","width":305,"height":203,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_PORTRAIT_16_9.jpg","width":640,"height":360,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_16_9.jpg","width":1024,"height":576,"fallback":false},{"ratio":"3_2","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_RETINA_PORTRAIT_3_2.jpg","width":640,"height":427,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_EVENT_DETAIL_PAGE_16_9.jpg","width":205,"height":115,"fallback":false},{"ratio":"16_9","url":"https://s1.ticketm.net/dam/a/15e/d2959961-cc88-4697-8aca-3911aeb8e15e_1557231_TABLET_LANDSCAPE_LARGE_16_9.jpg","width":2048,"height":1152,"fallback":false}],"classifications":[{"primary":true,"segment":{"id":"KZFzniwnSyZfZ7v7nJ","name":"Music"},"genre":{"id":"KnvZfZ7vAeA","name":"Rock"},"subGenre":{"id":"KZazBEonSMnZfZ7v6F1","name":"Pop"},"type":{"id":"KZAyXgnZfZ7v7nI","name":"Undefined"},"subType":{"id":"KZFzBErXgnZfZ7v7lJ","name":"Undefined"},"family":false}],"upcomingEvents":{"_total":77,"tmr":10,"mfx-za":2,"mfx-be":1,"mfx-dk":2,"mfx-nl":1,"ticketmaster":58,"mfx-cz":1,"mfx-ch":1,"mfx-pl":1},"_links":{"self":{"href":"/discovery/v2/attractions/K8vZ917G9e7?locale=en-us"}}}]}}]},"_links":{"self":{"href":"/discovery/v2/events.json?startDateTime=2021-11-17T12%3A00%3A00Z&size=12&city=Los+Angeles&classificationName=Music&endDateTime=2023-11-17T12%3A00%3A00Z&keyword=Justin"}},"page":{"size":12,"totalElements":2,"totalPages":1,"number":0}}
     * */
    public String searchEvent (@RequestParam("city") String city, @RequestParam("keyword") String keyword, @RequestParam("genre") String genre, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, Model model) throws Exception{
        String urlString = "https://app.ticketmaster.com/discovery/v2/events.json?size=12&city=" + city + "&keyword=" + keyword + "&classificationName="
                + genre + "&startDateTime=" + startDate + "T12:00:00Z" + "&endDateTime=" + endDate + "T12:00:00Z" + "&apikey=f3kuGY9d20QsgYWlhdQ0PwnnkeU2Mqym";

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Groupie/1.0");

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();
        model.addAttribute("events", response.toString());
        return "proposeEvent";
    }

    @GetMapping(value="/remove_receive_invites_lists")
    public @ResponseBody Map<String,String> remove_receive_invites_lists(HttpSession httpSession){
        User user=userRepository.findByUsername((String) httpSession.getAttribute("loginUser"));
        user.getReceive_invites_list().clear();
        user.getReject_invites_list().clear();
        user.getConfirmed_invites_list().clear();
        userRepository.save(user);

        Map<String,String> res=new HashMap<>();
        res.put("message","200");
        return res;
    }

    @GetMapping(value="reply-final-event")
    public String replyFinalEvent(@RequestParam("inviteId") Long inviteId, @RequestParam("status") String status, Model model, HttpSession httpSession) {
        User user=userRepository.findByUsername((String) httpSession.getAttribute("loginUser"));
        Invite invite = inviteRepository.findById(inviteId).get();
        List<Invite> received_invites = userRepository.findByUsername(user.getUsername()).getReceive_invites_list();
        List<Invite> rejected_invites = userRepository.findByUsername(user.getUsername()).getReject_invites_list();
        List<Invite> confirmed_invites = userRepository.findByUsername(user.getUsername()).getConfirmed_invites_list();
        received_invites.remove(invite);
        user.setReceive_invites_list(received_invites);
        if(status.equals("comfirm")){
            confirmed_invites.add(invite);
            user.setConfirmed_invites_list(confirmed_invites);
//            model.addAttribute("message", "invite confirmed");
        }else{
            rejected_invites.add(invite);
            user.setReject_invites_list(rejected_invites);
//            model.addAttribute("message", "invite rejected");
        }
        userRepository.save(user);
        invite.setStatus("finalized responded");
        inviteRepository.save(invite);
        return "redirect:/receive-groupDates";
    }

    @GetMapping(value="/get_finalized_invites_list")
    public @ResponseBody Map<String,List<Map<String,String>>> get_finalized_inviyes_list(HttpSession httpSession){
        User user=userRepository.findByUsername((String) httpSession.getAttribute("loginUser"));

        List<Map<String,String>> list=new ArrayList<>();
        List<Invite> invites = user.getReceive_invites_list();
        invites.addAll(user.getConfirmed_invites_list());
        for(Invite inv:invites){
            if(inv.getFinalEvent()!=null){
                Map<String,String> map=new HashMap<>();
                map.put("invite_id",inv.getId().toString());
                map.put("eventName",inv.getFinalEvent().getEventName());
                map.put("date",inv.getFinalEvent().getEventDate().toString());
                map.put("genre",inv.getFinalEvent().getGenre());
                map.put("location",inv.getFinalEvent().getLocation());
                map.put("sender",inv.getSender().getUsername());
                map.put("invite_name",inv.getInviteName());
                list.add(map);
            }

        }
        Map<String,List<Map<String,String>>> res=new HashMap<>();
        res.put("events",list);
        return res;
    }
}
