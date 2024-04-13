
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

    public DieticianController(DieticianRepository dieticianRepository){
        this.dieticianRepository = dieticianRepository;
    }

    @GetMapping(path = "/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("dietician", new Dietician());
        model.addAttribute("message", "");
        return "dietician_registration";
    }
    @PostMapping("/add")
    public String addNewDietician(@ModelAttribute("dietician") Dietician dietician, Model model) {
        // System.out.println("User details:");
        // System.out.println("gender: " + user.getUserGender());

        List<Dietician> alreadyExistingEmails = dieticianRepository.findByDieticianEmail(dietician.getDieticianEmail());
        if(!alreadyExistingEmails.isEmpty()){
            model.addAttribute("dietician", dietician);
            model.addAttribute("message", "An account linked to this email address already exists!");
            return "dietician_registration";
        }

        List<Dietician> dieticians= dieticianRepository.findAll();

        if(dieticians.isEmpty()){
            dietician.setDieticianId(1);
        }
        else{
            Integer newestDieticianID = (dieticians.get(dieticians.size() - 1)).getDieticianId();
            dietician.setDieticianId(newestDieticianID + 1);
        }

        dieticianRepository.save(dietician);
        return "redirect:/dietician/login";
    }



    @GetMapping(path = "/login")
    public String getLoginPage(Model model) {
        model.addAttribute("message", "");

        return "dietician_login";
    }
    
    @PostMapping(path = "/login")
    public String validateDietician(Model model, @RequestParam("email") String enteredEmail, @RequestParam("password") String enteredPassword, HttpServletRequest request){
        
        List<Dietician> existingDieticians = dieticianRepository.findByDieticianEmail(enteredEmail);

        if(existingDieticians.size() == 1 && (existingDieticians.get(0)).getDieticianPassword().equals(enteredPassword)){
            HttpSession session = request.getSession();
            session.setAttribute("loggedInDietician", existingDieticians.get(0));
            return "redirect:/dietician/home";
        }
        model.addAttribute("message", "Invalid email address and/or password");
        return "dietician_home";
    }

    @GetMapping("/home")
    public String dieticianHomePage(Model model, HttpServletRequest request) {
        
        Dietician currentdietician = (Dietician)request.getSession().getAttribute("loggedInDietician");
        if(currentdietician == null){
            return "redirect:/dietician/login";
        }
        else{
            return "dietician_home";
        }
    }


    @GetMapping("/updateDieticianInfo")
    public String showUpdateDieticianrInfoForm(Model model, HttpServletRequest request) {
        Dietician currentDietician = (Dietician) request.getSession().getAttribute("loggedInDietician");
        if (currentDietician == null) {
            return "redirect:/dietician/login";
        }
    
        Dietician dietician = new Dietician();
        dietician.setDieticianName(currentDietician.getDieticianName());
        dietician.setDieticianEmail(currentDietician.getDieticianEmail());
        dietician.setDieticianPhone(currentDietician.getDieticianPhone());
    
        model.addAttribute("dietician", dietician);
        return "updateDieticianInfo";
    }
    

    @PostMapping("/updateDietician")
    public String updateDietician(@ModelAttribute("dietician") Dietician updatedDietician, Model model, HttpServletRequest request) {
        Dietician currentDietician = (Dietician) request.getSession().getAttribute("loggedInDietician");
        if (currentDietician == null) {
            return "redirect:/dietician/login";
        }
        Dietician existingDietician = dieticianRepository.findById(currentDietician.getDieticianId()).orElse(null);
        if (existingDietician != null) {
            // Update Dietician information with the values from the updatedDietician object
            existingDietician.setDieticianName(updatedDietician.getDieticianName());
            existingDietician.setDieticianEmail(updatedDietician.getDieticianEmail());
            existingDietician.setDieticianPhone(updatedDietician.getDieticianPhone());

            dieticianRepository.save(existingDietician);
            Dietician updatedCurrentDietician = dieticianRepository.findById(currentDietician.getDieticianId()).orElse(null);
            if (updatedCurrentDietician != null) {
                request.getSession().setAttribute("loggedInDietician", updatedCurrentDietician);
            }
        }
        return "redirect:/dietician/home";
    }

}
