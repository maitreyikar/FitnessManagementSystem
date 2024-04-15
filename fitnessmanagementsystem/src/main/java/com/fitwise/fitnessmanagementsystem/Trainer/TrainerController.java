
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
    private HttpSession session;

    public TrainerController(TrainerRepository trainerRepository){
        this.trainerRepository = trainerRepository;
    }

    @GetMapping(path = "/register")
    public String getRegistrationPage(Model model) {
        model.addAttribute("trainer", new Trainer());
        model.addAttribute("message", "");
        return "trainer_registration";
    }

    
    @PostMapping("/add")
    public String addNewTrainer(@ModelAttribute("trainer") Trainer trainer, Model model) {
        // System.out.println("User details:");
        // System.out.println("gender: " + user.getUserGender());

        List<Trainer> alreadyExistingEmails = trainerRepository.findByTrainerEmail(trainer.getTrainerEmail());
        if(!alreadyExistingEmails.isEmpty()){
            model.addAttribute("trainer", trainer);
            model.addAttribute("message", "An account linked to this email address already exists!");
            //return "trainer_registration";
            return "redirect:/trainer/login";
        }

        // List<Trainer> trainers= trainerRepository.findAll();

        // if(trainers.isEmpty()){
        //     trainer.setTrainerId(1);
        // }
        // else{
        //     Integer newestTrainerID = (trainers.get(trainers.size() - 1)).getTrainerId();
        //     trainer.setTrainerId(newestTrainerID + 1);
        // }

        long pid = trainerRepository.count();

        trainer.setTrainerId(String.valueOf(pid));

        trainerRepository.save(trainer);
        return "redirect:/trainer/login";
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
            return "redirect:/trainer/home";
        }
        model.addAttribute("message", "Invalid email address and/or password");
        return "trainer_login";
    }

    @GetMapping("/home")
    public String trainerHomePage(Model model, HttpServletRequest request) {
        
        Trainer currenttrainer = (Trainer)request.getSession().getAttribute("loggedInTrainer");
        if(currenttrainer == null){
            return "redirect:/trainer/login";
        }
        else{
            model.addAttribute("currentTrainer", currenttrainer);
            return "trainer_home";
        }
    }   

    @GetMapping("/updateTrainerInfo")
    public String showUpdateTrainerrInfoForm(Model model, HttpServletRequest request) {
        Trainer currentTrainer = (Trainer) request.getSession().getAttribute("loggedInTrainer");
        if (currentTrainer == null) {
            return "redirect:/trainer/login";
        }
    
        Trainer trainer = new Trainer();
        trainer.setTrainerName(currentTrainer.getTrainerName());
        trainer.setTrainerEmail(currentTrainer.getTrainerEmail());
        trainer.setTrainerPhone(currentTrainer.getTrainerPhone());
    
        model.addAttribute("trainer", trainer);
        return "updateTrainerInfo";
    }
    

    @PostMapping("/updateTrainer")
    public String updateTrainer(@ModelAttribute("trainer") Trainer updatedTrainer, Model model, HttpServletRequest request) {
        Trainer currentTrainer = (Trainer) request.getSession().getAttribute("loggedInTrainer");
        if (currentTrainer == null) {
            return "redirect:/trainer/login";
        }
        Trainer existingTrainer = trainerRepository.findByTrainerId(currentTrainer.getTrainerId());
        if (existingTrainer != null) {
            // Update Trainer information with the values from the updatedTrainer object
            existingTrainer.setTrainerName(updatedTrainer.getTrainerName());
            existingTrainer.setTrainerEmail(updatedTrainer.getTrainerEmail());
            existingTrainer.setTrainerPhone(updatedTrainer.getTrainerPhone());

            trainerRepository.save(existingTrainer);
            Trainer updatedCurrentTrainer = trainerRepository.findByTrainerId(currentTrainer.getTrainerId());
            if (updatedCurrentTrainer != null) {
                request.getSession().setAttribute("loggedInTrainer", updatedCurrentTrainer);
            }
        }
        return "redirect:/trainer/home";
    }

}
