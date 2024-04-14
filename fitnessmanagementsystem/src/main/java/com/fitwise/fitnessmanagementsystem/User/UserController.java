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

import com.fitwise.fitnessmanagementsystem.DietPlan.DietPlan;
import com.fitwise.fitnessmanagementsystem.DietPlan.DietPlanRepository;
import com.fitwise.fitnessmanagementsystem.FitnessPlan.FitnessPlan;
import com.fitwise.fitnessmanagementsystem.FitnessPlan.FitnessPlanRepository;

@Controller
@RequestMapping(path = "/user")

public class UserController {

    @Autowired
    private UserRepository userRepository;
    private final DietPlanRepository dietPlanRepository;
    private final FitnessPlanRepository fitnessPlanRepository;
    private HttpSession session;        //change

    public UserController(UserRepository userRepository,DietPlanRepository dietPlanRepository, FitnessPlanRepository fitnessPlanRepository)
    {
        this.userRepository = userRepository;
        this.dietPlanRepository=dietPlanRepository;
        this.fitnessPlanRepository = fitnessPlanRepository;
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

    @GetMapping(path = "/main_home")
    public String main_home_page(Model model) {
        return "main_home";
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
            //session.setAttribute("userId", (existingUsers.get(0)).getUserId());     //change
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

    // @GetMapping("/updateUserInfo")
    // public String showUpdateUserInfoForm(Model model, HttpServletRequest request) {
    //     User currentUser = (User) request.getSession().getAttribute("loggedInUser");
    //     if (currentUser == null) {
    //         return "redirect:/user/login";
    //     }
    
    //     User user = new User();
    //     user.setUserName(currentUser.getUserName());
    //     user.setUserEmail(currentUser.getUserEmail());
    //     user.setUserPhone(currentUser.getUserPhone());
    //     user.setUserAge(currentUser.getUserAge());
    //     user.setUserHeight(currentUser.getUserHeight());
    //     user.setUserWeight(currentUser.getUserWeight());
    //     user.setUserGender(currentUser.getUserGender());
    
    //     model.addAttribute("user", user);
    //     return "updateUserInfo";
    // }
    

    // @PostMapping("/updateUser")
    // public String updateUser(@ModelAttribute("user") User updatedUser, Model model, HttpServletRequest request) {
    //     User currentUser = (User) request.getSession().getAttribute("loggedInUser");
    //     if (currentUser == null) {
    //         return "redirect:/user/login";
    //     }
    //     User existingUser = userRepository.findById(currentUser.getUserId()).orElse(null);
    //     if (existingUser != null) {
    //         // Update user information with the values from the updatedUser object
    //         existingUser.setUserName(updatedUser.getUserName());
    //         existingUser.setUserEmail(updatedUser.getUserEmail());
    //         existingUser.setUserPhone(updatedUser.getUserPhone());
    //         existingUser.setUserAge(updatedUser.getUserAge());
    //         existingUser.setUserHeight(updatedUser.getUserHeight());
    //         existingUser.setUserWeight(updatedUser.getUserWeight());
    //         existingUser.setUserGender(updatedUser.getUserGender());
    //         userRepository.save(existingUser);
    //         User updatedCurrentUser = userRepository.findById(currentUser.getUserId()).orElse(null);
    //         if (updatedCurrentUser != null) {
    //             request.getSession().setAttribute("loggedInUser", updatedCurrentUser);
    //         }
    //     }
    //     return "redirect:/user/home";
    // }

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

    
    @GetMapping("/selectdietplan")
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
            currentUser.setDietPlanId(selectedDietPlanId);
            currentUser.setDietPlanName(selectedDietPlan.getPlanName());
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


    @GetMapping("/selectfitnessplan")
    public String getSelectFitnessPlan(Model model) 
    {
        List<FitnessPlan> fitnessPlans = fitnessPlanRepository.findAll(); 
        model.addAttribute("fitnessplans", fitnessPlans);
        return "select_fitness_plan"; 
    }

    @PostMapping("/user/selectfitnessplan")
    public String handleSelectedFitnessPlan(@RequestParam(required = false) String selectedFitnessPlanId,
                                        HttpServletRequest request, HttpSession session)
    {  
                                         

        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) 
        {
        return "redirect:/user/login";
        }

        if (selectedFitnessPlanId != null) 
        {
            
        List<FitnessPlan> selectedPlans = fitnessPlanRepository.findByplanId(selectedFitnessPlanId);
        FitnessPlan selectedFitnessPlan = selectedPlans.isEmpty() ? null : selectedPlans.get(0);
        if (selectedFitnessPlan != null) 
        {
            currentUser.setFitnessPlanId(selectedFitnessPlanId);
            currentUser.setFitnessPlanName(selectedFitnessPlan.getPlanName());
            userRepository.save(currentUser);
            return "redirect:/success"; 
        }
        else {
            // Handle case where no fitness plan found with the ID
            return "redirect:/selectfitnessplan?error=noPlan"; // Example return for error
          }
 
    }
    return "redirect:/selectfitnessplan"; 
    
    

    }


    /*@PostMapping("/selectdietplan")
    public String handleSelectedDietPlan(@RequestParam(required = false) String selectedDietPlanId,
                                     HttpServletRequest request, HttpSession session) {
    // ... existing code
    
    if (selectedDietPlanId != null) {
        // Access the logged-in user from the session
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser != null) {
            // Update the user's selected plan details
            currentUser.setSelectedDietPlanId(selectedDietPlanId);
            // ... (Optional) Find the selected plan details from the database (if needed)
            List<DietPlan> selectedPlans = dietPlanRepository.findByplanId(selectedDietPlanId);
            DietPlan selectedDietPlan = selectedPlans.isEmpty() ? null : selectedPlans.get(0);
            if (selectedDietPlan != null) {
                currentUser.setSelectedDietPlanName(selectedDietPlan.getPlanName());
            }
            // Save the updated user information
            userRepository.save(currentUser);
            // ... (Optional) Redirect to a success page
            return "redirect:/success";
        }
    }
    // ... handle cases where no plan is found or user is not logged in
    return "redirect:/selectdietplan?error=noPlan"; 
}*/
   

}
