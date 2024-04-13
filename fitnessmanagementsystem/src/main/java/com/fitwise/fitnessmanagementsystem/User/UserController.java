package com.fitwise.fitnessmanagementsystem.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.*;

import com.fitwise.fitnessmanagementsystem.DietPlan.*;


@Controller
// @RequestMapping(path = "/user")

public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DietPlanRepository dietPlanRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @GetMapping(path="/user/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
    

    @GetMapping(path = "/user/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("message", "");
        return "user_registration";
    }

    @GetMapping(path = "/main_home")
    public String main_home_page(Model model) {
        return "main_home";
    }

    
    @PostMapping("/user/add")
    public String addNewUser(@ModelAttribute("user") User user, Model model) {
        
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

    @GetMapping(path = "/user/login")
    public String getLoginPage(Model model) {
        model.addAttribute("message", "");
        return "user_login";
    }
    
    @PostMapping(path = "/user/login")
    public String validateUser(Model model, @RequestParam("email") String enteredEmail, @RequestParam("password") String enteredPassword, HttpServletRequest request){
        
        List<User> existingUsers = userRepository.findByUserEmail(enteredEmail);
        // System.out.println(existingUsers.get(0).getUserEmail() +  ", " + existingUsers.get(0).getUserPassword());
        // System.out.println();

        if(!existingUsers.isEmpty() && (existingUsers.get(0)).getUserPassword().equals(enteredPassword)){
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", existingUsers.get(0));
            return "redirect:/user/home";
        }
        model.addAttribute("message", "Invalid email address and/or password");
        return "user_login";
    }

    @GetMapping("/user/home")
    public String userHomePage(Model model, HttpServletRequest request) {
        
        User currentuser = (User)request.getSession().getAttribute("loggedInUser");
        if(currentuser == null){
            return "redirect:/user/login";
        }
        else{
            return "user_home";
        }
    }

    @GetMapping("/user/updateUserInfo")
    public String showUpdateUserInfoForm(Model model, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/user/login";
        }    
        model.addAttribute("user", currentUser);
        return "updateUserInfo";
    }
    

    @PostMapping("/user/updateUser")
    public String updateUser(@ModelAttribute("user") User updatedUser, Model model, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/user/login";
        }
    
        //updatedUser.setUserId(currentUser.getUserId());
        userRepository.save(updatedUser);
        request.getSession().setAttribute("loggedInUser", updatedUser);
        return "redirect:/user/home";
    }
    @GetMapping("/user/selectdietplan")
    public String getSelectDietPlan(Model model) 
    {
        List<DietPlan> dietPlans = dietPlanRepository.findAll(); 
        model.addAttribute("dietplans", dietPlans);
        return "select_diet_plan"; 
    }

    @PostMapping("/user/selectdietplan")
    public String handleSelectedDietPlan(@RequestParam(required = false) String selectedDietPlanId,
                                        HttpServletRequest request, HttpSession session)
    {  
                                         

        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) 
        {
        return "redirect:/user/login";
        }

        if (selectedDietPlanId != null) 
        {
            
        List<DietPlan> selectedPlans = dietPlanRepository.findByplanId(selectedDietPlanId);
        DietPlan selectedDietPlan = selectedPlans.isEmpty() ? null : selectedPlans.get(0);
        if (selectedDietPlan != null) 
        {
            currentUser.setPlanId(selectedDietPlanId);
            currentUser.setPlanName(selectedDietPlan.getPlanName());
            userRepository.save(currentUser);
            return "redirect:/success"; 
        }
        else {
            // Handle case where no diet plan found with the ID
            return "redirect:/selectdietplan?error=noPlan"; // Example return for error
          }
 
    }
    return "redirect:/selectdietplan"; 
    
    

    }

    

}
