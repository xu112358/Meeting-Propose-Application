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
        return responseMap;
    }

    @GetMapping(value="/find-user-invite")
    public @ResponseBody List<Invite> findUserInvite(@RequestParam("username") String username) {
        List<Event> userEvents = userRepository.findByUsername(username).getUser_events_list();
        //filter out confirmed event
        List<Event> tmp = new ArrayList<>();
        for(Event event : userEvents){
             if(event.getStatus().equals("not confirmed")){
                 tmp.add(event);
             }
        }
        userEvents = tmp;
        List<Invite> invites = userRepository.findByUsername(username).getReceive_invites_list();
        //filter out confirmed invite --- not implemented
        for(Invite invite : invites){
            List<Event> inviteEvents = userEvents.stream().filter(userEvent -> userEvent.getInvites_which_hold_event().get(0).getId().equals(invite.getId())).collect(Collectors.toList());
            invite.setInvite_events_list(inviteEvents);
        }
        return invites;
    }

    @GetMapping(value="/search-event-by-invite-and-username")
    public @ResponseBody List<Event> eventSearch(@RequestParam("username") String username, @RequestParam("invite_id") Long inviteId) {
        List<Event> userEvents = userRepository.findByUsername(username).getUser_events_list();
        List<Event> inviteEvents = inviteRepository.findById(inviteId).get().getInvite_events_list();
        List<Event> useInviteEvent = new ArrayList<>();
        for(Event userEvent : userEvents){
             Event event = inviteEvents.stream().filter(inviteEvent -> userEvent.getId().equals(inviteEvent.getId())).findAny().orElse(null);
             if(event != null){
                 useInviteEvent.add(event);
             }
        }
        return useInviteEvent;
    }

    //this api has not been completed
    @PostMapping(value="/finalize-invite")
    public @ResponseBody Map<String, String> finalizeInvite(@RequestParam("invite_id") Long inviteId) {
        Optional<Invite> inviteOptional = inviteRepository.findById(inviteId);
        Invite invite = inviteOptional.get();
        List<Event> events = inviteOptional.get().getInvite_events_list();
        Map<String, List<Event>> eventMap = new HashMap<>();
        for(Event event :events) {
            String eventKey = event.getEventName() + event.getEventDate().toString();
            List<Event> tmp = eventMap.get(eventKey);
            if(tmp == null){
                tmp = new ArrayList<>();
                eventMap.put(eventKey, tmp);
            }
            tmp.add(event);
            eventMap.put(eventKey, tmp);
        }
        Map<String, Integer> eventEvaluation = new HashMap<>();
        // Iterating eventMap through for loop
        for (Map.Entry<String, List<Event>> eventKeyValue : eventMap.entrySet()) {
            //evaluate event feasibility

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
        User isBlocked = blockedUsers.stream().filter(tmp -> tmp.getId().equals(toBlock.getId())).findAny().orElse(null);
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
            event.setStatus("confirmed");
            eventRepository.save(event);
        }
        response.put("message", "Reply Sent");
        response.put("returnCode", "200");
        return response;
    }

}
