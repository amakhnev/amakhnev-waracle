package com.waracle.cakemgr.controller;

import com.waracle.cakemgr.entity.Cake;
import com.waracle.cakemgr.service.CakeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class CakeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CakeController.class);

    @Autowired
    private final CakeService service;

    public CakeController(CakeService service) {
        this.service = service;
    }

    @GetMapping("/cake")
    public List<Cake> getCakes(){
        LOGGER.info("received /cake get request");
        return service.getCakes();
    }

    @GetMapping("/cake/{id}")
    public Cake findById(@PathVariable UUID id) {
        LOGGER.info("received /cake/{id} get request");
        return service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find cake"));
    }

    @PostMapping("/cake")
    @ResponseStatus(CREATED)
    public Cake createCake(@Valid @RequestBody Cake cake){
        LOGGER.info("received /cake post request");
        return service.createCake(cake);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @DeleteMapping("/cake/{id}")
    public void deleteCake(@PathVariable UUID id){
        service.deleteCake(id);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public Map<String, String> handleNotFoundExceptions(
            EmptyResultDataAccessException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Error",ex.getMessage());
        return errors;
    }
}
