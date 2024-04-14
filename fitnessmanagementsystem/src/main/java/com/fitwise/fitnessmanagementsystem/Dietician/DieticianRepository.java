package com.fitwise.fitnessmanagementsystem.Dietician;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface DieticianRepository extends MongoRepository<Dietician, String>{
    public List<Dietician> findByDieticianEmail(String dieticianEmail);
}
