package com.fitwise.fitnessmanagementsystem.Meal;
import org.springframework.data.annotation.Id;
import  org.springframework.data.mongodb.core.index.Indexed;
// import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    @Id
    private Integer mealId;
    private String mealName;// "breakfast", "lunch", "dinner", "snack"
    @Indexed(unique = true) 
    private String description;
    private Integer noOfMeals;
    private Integer calories;
   
}