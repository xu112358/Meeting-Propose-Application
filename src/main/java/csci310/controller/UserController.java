package csci310.controller;

import csci310.entity.User;
import csci310.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public String createUser(@ModelAttribute("user") User user, Model model) {
        return "signin";
    }

}
