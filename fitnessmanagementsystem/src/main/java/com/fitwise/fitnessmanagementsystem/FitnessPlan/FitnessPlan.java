package com.fitwise.fitnessmanagementsystem.FitnessPlan;

//import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import  org.springframework.data.mongodb.core.index.Indexed;

//import com.fitwise.fitnessmanagementsystem.Exercise.Exercise;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FitnessPlan {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Exercise {
        @Id
        private String exerciseId;
        private String exerciseName;
    
        @Indexed(unique = true) 
        private Integer Duration;
        private Integer Reps;
        private Integer Sets;
    }

    @Id
    private String planId;
    private String planName;

    @Indexed(unique = true)
    private String planType;
    // private String Exercise1;  
    // private String Exercise2; 
    // private String Exercise3; 
    // private String Exercise4; 
    private List<FitnessPlan.Exercise> exerciseSet;
    private Integer totalDuration;
    private Integer caloriesBurnt;
   
}

