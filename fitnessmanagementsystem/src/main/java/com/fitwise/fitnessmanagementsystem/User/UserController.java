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
            return "user_home";
        }
    }

    @GetMapping("/updateUserInfo")
    public String showUpdateUserInfoForm(Model model, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/user/login";
        }
    
        User user = new User();
        user.setUserName(currentUser.getUserName());
        user.setUserEmail(currentUser.getUserEmail());
        user.setUserPhone(currentUser.getUserPhone());
        user.setUserAge(currentUser.getUserAge());
        user.setUserHeight(currentUser.getUserHeight());
        user.setUserWeight(currentUser.getUserWeight());
        user.setUserGender(currentUser.getUserGender());
    
        model.addAttribute("user", user);
        return "updateUserInfo";
    }
    

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User updatedUser, Model model, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        User existingUser = userRepository.findById(currentUser.getUserId()).orElse(null);
        if (existingUser != null) {
            // Update user information with the values from the updatedUser object
            existingUser.setUserName(updatedUser.getUserName());
            existingUser.setUserEmail(updatedUser.getUserEmail());
            existingUser.setUserPhone(updatedUser.getUserPhone());
            existingUser.setUserAge(updatedUser.getUserAge());
            existingUser.setUserHeight(updatedUser.getUserHeight());
            existingUser.setUserWeight(updatedUser.getUserWeight());
            existingUser.setUserGender(updatedUser.getUserGender());
            userRepository.save(existingUser);
            User updatedCurrentUser = userRepository.findById(currentUser.getUserId()).orElse(null);
            if (updatedCurrentUser != null) {
                request.getSession().setAttribute("loggedInUser", updatedCurrentUser);
            }
        }
        return "redirect:/user/home";
    }
    
    

}
