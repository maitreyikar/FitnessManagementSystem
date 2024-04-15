package com.fitwise.fitnessmanagementsystem.DietPlan;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;


@Controller 
@RequestMapping("/dietplan")
public class DietPlanController {

    private final DietPlanRepository dietPlanRepository;
    public DietPlanController(DietPlanRepository dietPlanRepository ) {
        this.dietPlanRepository= dietPlanRepository;
    }

    @GetMapping("/makedietplanner") 
    public String getMakeDietPlanner(Model model) 
    {
        model.addAttribute("dietplan", new DietPlan());
        return "make_dietplan";
    }

    @PostMapping("/savedietplanner")
    public String saveDietPlanner(@ModelAttribute("dietplan") DietPlan dietPlan,
                                  @RequestParam("mealName") List<String> mealNames,
                                  @RequestParam("description") List<String> descriptions,
                                  @RequestParam("noOfMeals") List<Integer> noOfMeals,
                                  @RequestParam("calories") List<Integer> calories) {
        
        List<DietPlan.Meal> mealList= new ArrayList<>();
        for (int i=0; i < mealNames.size(); i++){
            DietPlan.Meal meal = new DietPlan.Meal();
            String check = mealNames.get(i);
            if (!check.isEmpty()) {
                meal.setMealName(mealNames.get(i));
                meal.setDescription(descriptions.get(i));
                meal.setNoOfMeals(noOfMeals.get(i));
                meal.setCalories(calories.get(i));
                mealList.add(meal);
            }
        }

        long pid = dietPlanRepository.count();

        dietPlan.setPlanId(String.valueOf(pid));
        dietPlan.setMealSet(mealList);
        
        
        dietPlanRepository.save(dietPlan);
        return "redirect:/dietplan/successDP";
    }
    @GetMapping("/successDP")
    public String showSuccessPage(){
        return "successDP";
    }

    @GetMapping("/viewdietplans")
    public String viewDietPlan(Model model){
        List <DietPlan> dietPlans=dietPlanRepository.findAll();
        model.addAttribute("dietplans", dietPlans);
        return "view_diet_plans";
    }
     
}







