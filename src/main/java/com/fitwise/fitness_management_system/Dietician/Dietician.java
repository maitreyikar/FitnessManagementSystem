package com.fitwise.fitness_management_system.Dietician;

import org.springframework.data.annotation.Id;
import  org.springframework.data.mongodb.core.index.Indexed;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "dietician")
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
