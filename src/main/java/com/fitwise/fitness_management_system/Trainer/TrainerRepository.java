package com.fitwise.fitness_management_system.Trainer;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface TrainerRepository extends MongoRepository<Trainer, Integer>{
    public List<Trainer> findByTrainerEmail(String trainerEmail);
}