package com.fitwise.fitness_management_system.Dietician;

import org.springframework.data.annotation.Id;
import  org.springframework.data.mongodb.core.index.Indexed;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dietician {
    @Id
    private Integer dieticianId;
    private String dieticianName;

    @Indexed(unique = true)
    private String dieticianEmail;
    private String dieticianPassword;  
    private Long dieticianPhone;
    ///private String dieticianSpecialisation;

}
