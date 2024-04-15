package com.fitwise.fitnessmanagementsystem.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface UserRepository extends MongoRepository<User, Integer>{
    public List<User> findByUserEmail(String userEmail);
    // public String getFitnessplanId(Integer userId);
    
}
