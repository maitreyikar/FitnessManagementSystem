
package com.fitwise.fitnessmanagementsystem.Trainer;

import org.springframework.data.annotation.Id;
import  org.springframework.data.mongodb.core.index.Indexed;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trainer {
    @Id
    private Integer trainerId;
    private String trainerName;

    @Indexed(unique = true)
    private String trainerEmail;
    private String trainerPassword;  
    private Long trainerPhone;
    private String trainerSpecialisation;

}

