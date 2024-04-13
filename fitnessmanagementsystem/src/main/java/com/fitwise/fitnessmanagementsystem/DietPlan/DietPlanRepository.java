package com.fitwise.fitnessmanagementsystem.DietPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface DietPlanRepository extends MongoRepository<DietPlan, String> 
{
    public List<DietPlan> findByplanId(String planId);
}
 