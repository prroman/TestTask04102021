package com.rpr.demo.todoapp.ToDoApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PriceExceptionController   {

    @ExceptionHandler(value = IllegalCurrencyException.class)
    public ResponseEntity<Object> exception(IllegalCurrencyException exception) {
        return new ResponseEntity<>("Illegal currency, please choose from: [BTC], [ETH] or [XRP]", HttpStatus.NOT_FOUND);
    }
}
