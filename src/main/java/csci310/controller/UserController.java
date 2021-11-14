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


    @GetMapping({"/","/signin"})
    public String index(){
        return "signin";
    }
    @PostMapping({"/","/signin"})

    public String signin(@RequestParam("username") String username, @RequestParam(name="password") String password, Model model, HttpSession session){
        User user=userRepository.findByUsername(username);
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
                System.out.println("Login successfully");

                session.setAttribute("loginUser",username);
                return "redirect:/home";
            }
            else{
                model.addAttribute("error_message", "Username and password do not match!");
                model.addAttribute("username", username);
                model.addAttribute("password", password);

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
    public String createUser(@RequestParam("username") String username,@RequestParam(name="password") String password,@RequestParam(name="re_password",required = false) String re_password, Model model) {

        User user=new User();
        user.setUsername(username);
        user.setHashPassword(password);

        if (userRepository.findByUsername(user.getUsername()) == null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(user.getHashPassword());
            user.setHashPassword(encodedPassword);
            userRepository.save(user);
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
        for(String receiverUsername : receiversUsername){
            receivers.add(userRepository.findByUsername(receiverUsername));
        }
        //Check if sender is on the block list of receiver
        for(User receiver : receivers){
            List<User> receiverBlockList = userRepository.findByUsername(receiver.getUsername()).getBlock_list();
            for(User userOnBlockList : receiverBlockList){
                if(sender.getId() == userOnBlockList.getId()){
                    responseMap.put("message", "you are blocked by " + receiver.getUsername());
                    responseMap.put("returnCode", "400");
                    return responseMap;
                }
            }
        }

        Invite invite = new Invite();
        invite.setInviteName(inviteName);
        invite.setCreateDate(new Date());
        invite.setSender(sender);
        invite.setReceivers((receivers));
        inviteRepository.save(invite);
        //can cause problem with multithreaded server
        invite = inviteRepository.findTopByOrderByIdDesc();
        for(int i = 0; i < receivers.size(); i ++){
            for(int j = 0; j < events.size(); j ++){
                Event event = new Event(events.get(j));
                event.setStatus("not confirmed");
                event.setInvites_which_hold_event(new ArrayList<Invite>(
                        Arrays.asList(invite)));
                event.setUsers_who_hold_event(new ArrayList<User>(
                        Arrays.asList(receivers.get(i))));
                eventRepository.save(event);
            }
        }

        responseMap.put("message", "Invite Sent");
        responseMap.put("returnCode", "200");
        return responseMap;
    }

    @GetMapping(value="/find-received-invite")
    public @ResponseBody List<Invite> findUserInvite(@RequestParam("username") String username) {
        User sender = userRepository.findByUsername(username);
        List<Event> userEvents = userRepository.findByUsername(username).getUser_events_list();
        List<Invite> invites = userRepository.findByUsername(username).getReceive_invites_list();
        //filter out confirmed event
        List<Event> tmp = new ArrayList<>();
        for(Event event : userEvents){
             if(event.getStatus().equals("not confirmed")){
                 tmp.add(event);
             }
        }
        userEvents = tmp;
        List<Invite> invitesResult = new ArrayList<>();
        //filter out confirmed invite --- not implemented
        for(Invite invite : invites){

            //List<Event> inviteEvents = userEvents.stream().filter(userEvent -> userEvent.getInvites_which_hold_event().get(0).getId().equals(invite.getId())).collect(Collectors.toList());
            List<Event> inviteEvents = new ArrayList<>();
            for(Event userEvent : userEvents){
                if(userEvent.getInvites_which_hold_event().get(0).getId().equals(invite.getId())){
                    inviteEvents.add(userEvent);
                }
            }
            invite.setInvite_events_list(inviteEvents);
            invite.setSender(sender);
            invitesResult.add(invite);
        }
        return invitesResult;
    }

    @GetMapping(value="/find-sent-invite")
    public @ResponseBody Map<String, List<Invite>> findSentInvite(@RequestParam("username") String username) {
        List<Invite> invites = userRepository.findByUsername(username).getSend_invites_list();
        Map<String, List<Invite>> responseMap = new HashMap<>();
        List<Invite> resultInvites = new ArrayList<>();
        for(Invite invite : invites){
            List<Event> events =  inviteRepository.findById(invite.getId()).get().getInvite_events_list();
            Invite tmp = invite;
            tmp.setInvite_events_list(events);
            resultInvites.add(tmp);
        }
        responseMap.put("invites", resultInvites);
        return responseMap;
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
    @PostMapping(value="/finalize-invite")
    public @ResponseBody Map<String, String> finalizeInvite(@RequestParam("invite_id") Long inviteId) {
        Optional<Invite> inviteOptional = inviteRepository.findById(inviteId);
        //get invite
        List<Event> events = inviteOptional.get().getInvite_events_list();
        Map<String, List<Event>> eventMap = new HashMap<>();
        for(Event event :events) {
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
                if(event.getStatus().equals("yes")){
                    multiply = 1;
                }else if(event.getStatus().equals("maybe")){
                    multiply = 0.5;
                }else if(event.getStatus().equals("no")){
                    multiply = 0;
                }
                evaluate += multiply * event.getPreference();
            }
            eventEvaluation.put(eventKeyValue.getKey(), evaluate);
        }
        //find event with highest evaluation
        double maxEvaluate = 0;
        String maxEventKey = "";
        for (Map.Entry<String, Double> eventKeyValue : eventEvaluation.entrySet()) {
            if(eventKeyValue.getValue() > maxEvaluate) {
                maxEvaluate = eventKeyValue.getValue();
                maxEventKey = eventKeyValue.getKey();
            }
        }
        for (Map.Entry<String, List<Event>> eventKeyValue : eventMap.entrySet()) {
            //what to do after finalizing event
        }
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Invite Finalized");
        return responseMap;
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
    public @ResponseBody Map<String, String> deleteBlockedUser(@RequestParam("username") String username, @RequestParam("blocked") String blockedUsername) {
        Map<String, String> response = new HashMap<>();
        List<User> users = userRepository.findByUsername(username).getBlock_list();
        for(int i = 0; i < users.size(); i ++){
            if(users.get(i).getUsername().equals(blockedUsername)){
                users.remove(i);
            }
        }
        User user = userRepository.findByUsername(username);
        user.setBlock_list(users);
        userRepository.save(user);
        response.put("message", "User unblocked");
        response.put("returnCode", "200");
        return response;
    }

    @PostMapping(value = "/usernameStartingWith", consumes = "application/json")
    @ResponseBody
    public Map<String,List<String>> usernameStartingWith(@RequestBody Map<String,Object> map) throws JsonProcessingException {
        String name=(String) map.get("name");

        List<User> users=userRepository.findByUsernameStartingWith(name);

        List<String> usernames=new ArrayList<>();

        for(User obj:users){
            usernames.add(obj.getUsername());
        }

        Map<String, List<String>> result = new HashMap<>();

        result.put("names",usernames);
        return result;

    }

    @PostMapping(value="/reply-invite")
    public @ResponseBody Map<String, String> replyInvite (@RequestParam("username") String username, @RequestBody List<Event> events) throws JsonProcessingException {

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

    @PostMapping(value="/update-unavailable-date")
    public @ResponseBody Map<String, String> updateUserDateRange (@RequestParam("username") String username, @RequestParam("startDate") String start, @RequestParam("endDate") String end) throws JsonProcessingException {
        return new HashMap<>();
    }
}
