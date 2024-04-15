package com.fitwise.fitnessmanagementsystem.FitnessPlan;

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


// @Controller
// @RequestMapping(path = "/fitnessplan")

// public class FitnessPlanController {
//     @Autowired
//     private FitnessPlanRepository fitnessPlanRepository;

//     public FitnessPlanController(FitnessPlanRepository fitnessPlanRepository){
//         this.fitnessPlanRepository = fitnessPlanRepository;
//     }

//     @GetMapping(path = "/makeFP")
//     public String getMakeFPPage(Model model) {
//         // model.addAttribute("user", new FitnessPlan());
//         // model.addAttribute("message", "");
//         model.addAttribute("fitnessplan", new FitnessPlan());
//         return "make_fitnessplan";
//     }
    
//     @PostMapping("/saveFitnessPlan")
//     public String addFitnessPlan(@ModelAttribute("fitnessplan") FitnessPlan fitnessPlan,
//                               @RequestParam("ExerciseName[]") List<String> exerciseName,
//                               @RequestParam("Duration[]") List<Integer> duration,
//                               @RequestParam("Reps[]") List<Integer> reps,
//                               @RequestParam("Sets[]") List<Integer> sets) {
//     // Process the received data
//     // Save the fitness plan and exercise details to the database
//     //return "redirect:/success"; // Redirect to success page


//         // Create Exercise objects and add them to the FitnessPlan
//         ArrayList<FitnessPlan.Exercise> exercises = new ArrayList<>();
//         for (int i = 0; i < exerciseName.size(); i++) {
//             FitnessPlan.Exercise exercise = new FitnessPlan.Exercise();
//             exercise.setExerciseName(exerciseName.get(i));
//             exercise.setDuration(duration.get(i));
//             exercise.setReps(reps.get(i)); 
//             exercise.setSets(sets.get(i)); 
//             exercises.add(exercise);
//         }
//         List<FitnessPlan.Exercise> exerciseSet = fitnessPlan.getExerciseSet();
//         for (FitnessPlan.Exercise exercise : exerciseSet) {
//             System.out.println("Exercise Name: " + exercise.getExerciseName());
//             System.out.println("Duration: " + exercise.getDuration());
//             System.out.println("Reps: " + exercise.getReps());
//             System.out.println("Sets: " + exercise.getSets());
//         }

//         // ArrayList<FitnessPlan.Exercise> exerciseArrayList = new ArrayList<>(exercises);
//         // fitnessPlan.setExerciseSet(exerciseArrayList);
//         fitnessPlan.setExerciseSet(exercises);

//         // Save the FitnessPlan object to the database
//         fitnessPlanRepository.save(fitnessPlan);

//         return "redirect:/fitnessplan/success"; // Redirect to a success page
//     }

//     @GetMapping("/success")
//     public String showSuccessPage() {
//         return "success"; // Assuming "success.html" is the name of your success page template
//     }

// }

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import com.fitwise.fitnessmanagementsystem.Trainer.Trainer;
import com.fitwise.fitnessmanagementsystem.Trainer.TrainerRepository;

@Controller
@RequestMapping("/fitnessplan")
public class FitnessPlanController {

    @Autowired
    private final FitnessPlanRepository fitnessPlanRepository;

    public FitnessPlanController(FitnessPlanRepository fitnessPlanRepository) {
        this.fitnessPlanRepository = fitnessPlanRepository;
    }

    @GetMapping("/makeFP")
    public String getMakeFPPage(Model model, @RequestParam("currentTrainer") String currentTrainer) {
        model.addAttribute("fitnessplan", new FitnessPlan());
        model.addAttribute("currentTrainer", currentTrainer);
        return "make_fitnessplan";
    }

    @PostMapping("/saveFitnessPlan")
    public String saveFitnessPlan(@ModelAttribute("fitnessplan") FitnessPlan fitnessPlan,
                                  @RequestParam("currentTrainer") String currentTrainer,
                                  @RequestParam("exerciseName") List<String> exerciseNames,
                                  @RequestParam("duration") List<Integer> durations,
                                  @RequestParam("reps") List<Integer> reps,
                                  @RequestParam("sets") List<Integer> sets) {

        List<FitnessPlan.Exercise> exerciseList = new ArrayList<>();
        for (int i = 0; i < exerciseNames.size(); i++) {
            FitnessPlan.Exercise exercise = new FitnessPlan.Exercise();
            String check = exerciseNames.get(i);
            if (check!="") {
                String id = String.valueOf(i);
                exercise.setExerciseId(id);
                exercise.setExerciseName(exerciseNames.get(i));
                exercise.setDuration(durations.get(i));
                exercise.setReps(reps.get(i));
                exercise.setSets(sets.get(i));
                exerciseList.add(exercise);
                fitnessPlan.setTrainerName(currentTrainer);
            };
            
        }
            long pid = fitnessPlanRepository.count();

            fitnessPlan.setPlanId(String.valueOf(pid));

            fitnessPlan.setExerciseSet(exerciseList);

            fitnessPlanRepository.save(fitnessPlan);

            return "redirect:/fitnessplan/success";
        }
            
    

    @GetMapping("/success")
    public String showSuccessPage() {
        return "successFP";
    }
    @GetMapping("/viewfitnessplans")
    public String getviewFPPage(Model model) {
        List <FitnessPlan> fitnessPlans=fitnessPlanRepository.findAll();
        model.addAttribute("fitnessplans", fitnessPlans);
        return "view_fitness_plans";
    }
}
