package com.fitwise.fitnessmanagementsystem.Trainer;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;


public interface TrainerRepository extends MongoRepository<Trainer, String>{
    public List<Trainer> findByTrainerEmail(String trainerEmail);
    public Trainer findByTrainerId(String trainerId);
}
