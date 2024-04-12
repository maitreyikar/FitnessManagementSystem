package com.fitwise.fitness_management_system.Dietician;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface DieticianRepository extends MongoRepository<Dietician, Integer>{
    public List<Dietician> findByDieticianEmail(String dieticianEmail);
}
