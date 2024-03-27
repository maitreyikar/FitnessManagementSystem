package com.fitwise.fitness_management_system.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public String addNewUser(@ModelAttribute("user") User user) {
        System.out.println("User details:");
        System.out.println("gender: " + user.getUserGender());
        userRepository.save(user);
        return "redirect:/user/all";
    }

  @GetMapping(path="/all")
  public @ResponseBody Iterable<User> getAllUsers() {
    // This returns a JSON or XML with the users
    return userRepository.findAll();
  }

  @GetMapping(path = "/register")
  public String getRegistrationPage(Model model) {
    model.addAttribute("user", new User());
    return "user_registration";
  }
  @GetMapping(path = "/login")
    public String getLoginPage(Model model) {
      return "user_login";
  }

  
  
}