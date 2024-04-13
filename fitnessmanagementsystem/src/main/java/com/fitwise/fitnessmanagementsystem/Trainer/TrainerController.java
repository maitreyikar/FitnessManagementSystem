
package com.fitwise.fitnessmanagementsystem.Trainer;

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
@RequestMapping(path = "/trainer")

public class TrainerController {
    @Autowired
    private TrainerRepository trainerRepository;

    public TrainerController(TrainerRepository trainerRepository){
        this.trainerRepository = trainerRepository;
    }

    @GetMapping(path = "/login")
    public String getLoginPage(Model model) {
        model.addAttribute("message", "");
        return "trainer_login";
    }
    
    @PostMapping(path = "/login")
    public String validateTrainer(Model model, @RequestParam("email") String enteredEmail, @RequestParam("password") String enteredPassword, HttpServletRequest request){
        
        List<Trainer> existingTrainers = trainerRepository.findByTrainerEmail(enteredEmail);

        if(existingTrainers.size() == 1 && (existingTrainers.get(0)).getTrainerPassword().equals(enteredPassword)){
            HttpSession session = request.getSession();
            session.setAttribute("loggedInTrainer", existingTrainers.get(0));
            return "redirect:/fitnessplan/makeFP";
        }
        model.addAttribute("message", "Invalid email address and/or password");
        return "trainer_login";
    }

}
