package com.fitwise.fitnessmanagementsystem.Exercise;

import org.springframework.data.annotation.Id;
import  org.springframework.data.mongodb.core.index.Indexed;

import com.fitwise.fitnessmanagementsystem.Exercise.Exercise;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {
    @Id
    private Integer exerciseId;
    private String exerciseName;

    @Indexed(unique = true) 
    private Integer Duration;
    private Integer Reps;
    private Integer Sets;
}