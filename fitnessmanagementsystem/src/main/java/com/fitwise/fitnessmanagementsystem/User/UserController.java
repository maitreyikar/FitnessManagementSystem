package com.fitwise.fitnessmanagementsystem.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.*;


@Controller
@RequestMapping(path = "/user")

public class UserController {
    @Autowired
    private UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping(path = "/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("message", "");
        return "user_registration";
    }

    
    @PostMapping("/add")
    public String addNewUser(@ModelAttribute("user") User user, Model model) {
        // System.out.println("User details:");
        // System.out.println("gender: " + user.getUserGender());

        List<User> alreadyExistingEmails = userRepository.findByUserEmail(user.getUserEmail());
        if(!alreadyExistingEmails.isEmpty()){
            model.addAttribute("user", user);
            model.addAttribute("message", "An account linked to this email address already exists!");
            return "user_registration";
        }

        List<User> users= userRepository.findAll();

        if(users.isEmpty()){
            user.setUserId(1);
        }
        else{
            Integer newestUserID = (users.get(users.size() - 1)).getUserId();
            user.setUserId(newestUserID + 1);
        }

        userRepository.save(user);
        return "redirect:/user/login";
    }

    @GetMapping(path = "/login")
    public String getLoginPage(Model model) {
        model.addAttribute("message", "");
        return "user_login";
    }
    
    @PostMapping(path = "/login")
    public String validateUser(Model model, @RequestParam("email") String enteredEmail, @RequestParam("password") String enteredPassword, HttpServletRequest request){
        
        List<User> existingUsers = userRepository.findByUserEmail(enteredEmail);
        // System.out.println(existingUsers.get(0).getUserEmail() +  ", " + existingUsers.get(0).getUserPassword());
        // System.out.println();

        if(existingUsers.size() == 1 && (existingUsers.get(0)).getUserPassword().equals(enteredPassword)){
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", existingUsers.get(0));
            return "redirect:/user/home";
        }
        model.addAttribute("message", "Invalid email address and/or password");
        return "user_login";
    }

    @GetMapping("/home")
    public String userHomePage(Model model, HttpServletRequest request) {
        
        User currentuser = (User)request.getSession().getAttribute("loggedInUser");
        if(currentuser == null){
            return "redirect:/user/login";
        }
        else{
            return "home";
        }
    }

    // @GetMapping("/updateUserInfo")
    // public String showUpdateUserInfoForm(Model model) {
    //     model.addAttribute("user", new User());
    //     return "updateUserInfo";
    // }

    // @PostMapping("/updateUser")
    // public String updateUser(@ModelAttribute User user) {
    //     // Retrieve the existing user from the database using userId
    //     User existingUser = userRepository.findById(user.getUserId()).orElse(null);
    //     if (existingUser != null) {
    //         // Update the fields that the user has provided
    //         existingUser.setUserName(user.getUserName());
    //         existingUser.setUserEmail(user.getUserEmail());
    //         existingUser.setUserPhone(user.getUserPhone());
    //         existingUser.setUserAge(user.getUserAge());
    //         existingUser.setUserHeight(user.getUserHeight());
    //         existingUser.setUserWeight(user.getUserWeight());
    //         existingUser.setUserGender(user.getUserGender());
    //         // Save the updated user information
    //         userRepository.save(existingUser);
    //     }
    //     return "redirect:/user/updateUserInfo";
    // }

}
