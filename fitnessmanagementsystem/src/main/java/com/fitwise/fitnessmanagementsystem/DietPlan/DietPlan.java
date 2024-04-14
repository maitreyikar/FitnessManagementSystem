package com.fitwise.fitnessmanagementsystem.DietPlan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class DietPlan {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor

    public static class Meal{
        @Id
        private String mealId;
        private String mealName;

        @Indexed(unique = true)
        private String description;
        private Integer noOfMeals;
        private Integer calories;

    }

    @Id
    private Integer planId;
    private String planName;

    @Indexed(unique=true)
    private String planType;
    private List<DietPlan.Meal> mealSet;
    private String dieticianName; 
    private Integer totalCalories;
    
}




    
    