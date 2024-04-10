package com.fitwise.fitnessmanagementsystem.Dietician;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface DieticianRepository extends MongoRepository<Dietician, Integer>{
    public List<Dietician> findByDieticianEmail(String dieticianEmail);
}
