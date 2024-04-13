package com.fitwise.fitness_management_system.DietPlan;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface DietPlanRepository extends MongoRepository<DietPlan, String> 
{
    public List<DietPlan> findByplanId(String planId);
}
 