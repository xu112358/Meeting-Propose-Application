package csci310.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

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
                event.setStatus("not comfirmed");
                event.setInvites_which_hold_event(new ArrayList<Invite>(
                        Arrays.asList(invite)));
                event.setUsers_who_hold_event(new ArrayList<User>(
                        Arrays.asList(receivers.get(i))));
                eventRepository.save(event);
            }
        }
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Invite Sent");
        return responseMap;
    }

    @GetMapping(value="/find-user-invite")
    public @ResponseBody List<Invite> findUserInvite(@RequestParam("username") String username) {
        return userRepository.findByUsername(username).getSend_invites_list();
    }

    @GetMapping(value="/search-event")
    public @ResponseBody List<Event> eventSearch(@RequestParam("username") String username) {
        return userRepository.findByUsername(username).getUser_events_list();
    }

    @PostMapping(value="/finalize-invite")
    public @ResponseBody Map<String, String> finalizeInvite(@RequestParam("username") String username, @RequestParam("invite_id") String inviteId) {
        return new HashMap<>();
    }
}
