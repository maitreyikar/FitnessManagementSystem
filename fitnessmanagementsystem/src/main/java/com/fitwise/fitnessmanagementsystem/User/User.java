package com.fitwise.fitnessmanagementsystem.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import  org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fitwise.fitnessmanagementsystem.DietPlan.DietPlan;
import com.fitwise.fitnessmanagementsystem.FitnessPlan.FitnessPlan;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private Integer userId;
    private String userName;

    @Indexed(unique = true)
    private String userEmail;
    private String userPassword;  
    private Long userPhone;
    private Integer userAge;
    private Float userHeight;
    private Float userWeight;
    private Gender userGender;
    private String dietplanId;
    private String fitnessplanId;


}
