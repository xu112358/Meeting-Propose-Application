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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public String createUser(@RequestParam("username") String username,@RequestParam(name="password") String password,@RequestParam(name="re_password",required = false) String re_password,@RequestParam(name="fname") String fname,@RequestParam(name="lname") String lname, Model model) {

        User user=new User();
        user.setUsername(username);
        user.setFirstName(fname);
        user.setLastName(lname);
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

    @PostMapping(value="/send-invitation")
    public String sendInvite(@RequestBody InviteModel inviteModel, Model model) {
        String senderUsername = inviteModel.getSender();
        List<String> receiversUsername = inviteModel.getReceivers();
        String inviteName = inviteModel.getInvite_name();
        User sender = userRepository.findByUsername(senderUsername);
        List<Event> events = inviteModel.getEvents();
        List<User> receivers = new ArrayList<>();
        for(String receiverUsername : receiversUsername){
            receivers.add(userRepository.findByUsername(receiverUsername));
        }
        List<Event> tmp = new ArrayList<>();
        List<Invite> invites = new ArrayList<>();
        for(Event event : events){
            System.out.println(event);
            event.getUsers_who_hold_event().add(sender);
            eventRepository.save(event);
            event = eventRepository.findTopByOrderByIdDesc();
            tmp.add(event);
            //add event to receiver
        }
        events = tmp;
        for(int i = 0; i < receivers.size(); i ++){
            Invite invite = new Invite();
            invite.setStatus("not comfirmed");
            invite.setSender(sender);
            invite.getReceivers().add(receivers.get(i));
            invite.setInviteName(inviteName);
            invite.setCreateDate(new Date());
            for(int j = 0; j < events.size(); j ++){
                invite.getInvite_events_list().add(events.get(j));
            }
            inviteRepository.save(invite);
        }
        model.addAttribute("message", "Invite Sent");
        return "home";
    }







    }
