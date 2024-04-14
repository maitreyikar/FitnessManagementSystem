
package com.fitwise.fitnessmanagementsystem.Dietician;

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
@RequestMapping(path = "/dietician")

public class DieticianController {
    @Autowired
    private DieticianRepository dieticianRepository;
    private HttpSession session;


    public DieticianController(DieticianRepository dieticianRepository){
        this.dieticianRepository = dieticianRepository;
    }

    @GetMapping(path = "/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("dietician", new Dietician());
        model.addAttribute("message", "");
        return "dietician_registration";
    }
    
    @GetMapping(path = "/login")
    public String getLoginPage(Model model) {
        model.addAttribute("message", "");

        return "dietician_login";
    }
    

    @PostMapping("/add")
    public String addNewTrainer(@ModelAttribute("trainer") Dietician dietician, Model model) {
        // System.out.println("User details:");
        // System.out.println("gender: " + user.getUserGender());

        List<Dietician> alreadyExistingEmails = dieticianRepository.findByDieticianEmail(dietician.getDieticianEmail());
        if(!alreadyExistingEmails.isEmpty()){
            model.addAttribute("dietician", dietician);
            model.addAttribute("message", "An account linked to this email address already exists!");
            //return "trainer_registration";
            return "redirect:/dietician/login";
        }

        // List<Trainer> trainers= trainerRepository.findAll();

        // if(trainers.isEmpty()){
        //     trainer.setTrainerId(1);
        // }
        // else{
        //     Integer newestTrainerID = (trainers.get(trainers.size() - 1)).getTrainerId();
        //     trainer.setTrainerId(newestTrainerID + 1);
        // }

        long pid = dieticianRepository.count();

        dietician.setDieticianId(String.valueOf(pid));

        dieticianRepository.save(dietician);
        return "redirect:/dietician/login";
    }

    // @GetMapping(path = "/login")
    // public String getDieticianLoginPage(Model model) {
    //     model.addAttribute("message", "");
    //     return "dietician_login";
    // }

    @PostMapping(path = "/login")
    public String validateTrainer(Model model, @RequestParam("email") String enteredEmail, @RequestParam("password") String enteredPassword, HttpServletRequest request){
        
        List<Dietician> existingDieticians = dieticianRepository.findByDieticianEmail(enteredEmail);

        if(existingDieticians.size() == 1 && (existingDieticians.get(0)).getDieticianPassword().equals(enteredPassword)){
            HttpSession session = request.getSession();
            session.setAttribute("loggedInDietician", existingDieticians.get(0));
            return "redirect:/dietician/home";
        }
        model.addAttribute("message", "Invalid email address and/or password");
        return "dietician_login";
    }

    @GetMapping("/home")
    public String dieticianHomePage(Model model, HttpServletRequest request) {
        
        Dietician currentdietician = (Dietician)request.getSession().getAttribute("loggedInDietician");
        if(currentdietician == null){
            return "redirect:/dietician/login";
        }
        else{
            model.addAttribute("currentDietician", currentdietician);
            return "dietician_home";
        }
    }

}
