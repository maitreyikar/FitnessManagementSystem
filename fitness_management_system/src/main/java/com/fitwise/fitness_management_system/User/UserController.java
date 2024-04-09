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

  @GetMapping("/updateUserInfo")
  public String showUpdateUserInfoForm(Model model) {
      model.addAttribute("user", new User());
      return "updateUserInfo";
  }
  @GetMapping("/home")
  public String userHomePage(Model model) {
      return "user_home";
  }

  @PostMapping("/updateUser")
  public String updateUser(@ModelAttribute User user) {
      // Retrieve the existing user from the database using userId
      User existingUser = userRepository.findById(user.getUserId()).orElse(null);
      if (existingUser != null) {
          // Update the fields that the user has provided
          existingUser.setUserName(user.getUserName());
          existingUser.setUserEmail(user.getUserEmail());
          existingUser.setUserPhone(user.getUserPhone());
          existingUser.setUserAge(user.getUserAge());
          existingUser.setUserHeight(user.getUserHeight());
          existingUser.setUserWeight(user.getUserWeight());
          existingUser.setUserGender(user.getUserGender());
          // Save the updated user information
          userRepository.save(existingUser);
      }
      return "redirect:/user/updateUserInfo";
  }

  
  
}