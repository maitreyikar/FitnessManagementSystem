package com.fitwise.fitness_management_system.FitnessPlan;

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


import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/fitnessplan")
public class FitnessPlanController {

    private final FitnessPlanRepository fitnessPlanRepository;

    @Autowired
    public FitnessPlanController(FitnessPlanRepository fitnessPlanRepository) {
        this.fitnessPlanRepository = fitnessPlanRepository;
    }

    @GetMapping("/makeFP")
    public String getMakeFPPage(Model model) {
        model.addAttribute("fitnessplan", new FitnessPlan());
        return "make_fitnessplan";
    }

    @PostMapping("/saveFitnessPlan")
    public String saveFitnessPlan(@ModelAttribute("fitnessplan") FitnessPlan fitnessPlan,
                                  @RequestParam("exerciseName") List<String> exerciseNames,
                                  @RequestParam("duration") List<Integer> durations,
                                  @RequestParam("reps") List<Integer> reps,
                                  @RequestParam("sets") List<Integer> sets) {

        List<FitnessPlan.Exercise> exerciseList = new ArrayList<>();
        for (int i = 0; i < exerciseNames.size(); i++) {
            FitnessPlan.Exercise exercise = new FitnessPlan.Exercise();
            String check = exerciseNames.get(i);
            if (check!="") {
                exercise.setExerciseName(exerciseNames.get(i));
                exercise.setDuration(durations.get(i));
                exercise.setReps(reps.get(i));
                exercise.setSets(sets.get(i));
                exerciseList.add(exercise);
            };
            
        }

        fitnessPlan.setExerciseSet(exerciseList);

        fitnessPlanRepository.save(fitnessPlan);

        return "redirect:/fitnessplan/successFP";
    }

    @GetMapping("/successFP")
    public String showSuccessPage() {
        return "successFP";
    }
}