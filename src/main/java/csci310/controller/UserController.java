package csci310.controller;

import csci310.entity.User;
import csci310.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/signup", method = RequestMethod.GET)
    public String createSignupForm(Model model) {
        model.addAttribute("welcome", "welcome");
        return "signup.html";
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String createUser(@RequestBody User user, Model model) {
        System.out.println(user);
        System.out.println((user.getUsername()));
        System.out.println(userRepository.findByUsername(user.getUsername()));
        if (userRepository.findByUsername(user.getUsername()) == null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(user.getHashPassword());
            user.setHashPassword(encodedPassword);
            userRepository.save(user);
            model.addAttribute("message", "Sign Up Successfully!");
            return "signin.html";
        }
        else {
            model.addAttribute("message", "Username is taken. Try another one.");
            return "signup.html";
        }
    }

}
