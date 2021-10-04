package com.rpr.demo.todoapp.ToDoApp.repository;

import com.rpr.demo.todoapp.ToDoApp.model.Price;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface PriceRepository extends MongoRepository<Price, String> {

    @Aggregation(pipeline = { "{ '$group': { '_id' : '$curr1' } }" })
    List<String> findDistinctCurrencies();
}

