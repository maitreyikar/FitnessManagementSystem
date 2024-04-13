package com.fitwise.fitness_management_system.FitnessPlan;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface FitnessPlanRepository extends MongoRepository<FitnessPlan, String>{
    public List<FitnessPlan> findByplanId(String planId);
} 