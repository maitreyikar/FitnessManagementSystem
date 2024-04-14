package com.fitwise.fitnessmanagementsystem.User;

import org.springframework.data.annotation.Id;
import  org.springframework.data.mongodb.core.index.Indexed;
import lombok.*;

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

    // private String selectedDietPlanId;
    // private String selectedDietPlanName;
    // private String selectedFitnessPlanId;
    // private String selectedFitnessPlanName;
    private String dietPlanId;
    private String dietPlanName;
    private String fitnessPlanId;
    private String fitnessPlanName;
}
