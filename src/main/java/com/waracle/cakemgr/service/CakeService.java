package com.waracle.cakemgr.service;

import com.waracle.cakemgr.entity.Cake;
import com.waracle.cakemgr.repository.CakeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CakeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CakeService.class);
@Autowired
    private final CakeRepository repository;

    public CakeService(CakeRepository repository) {
        this.repository = repository;
    }

    public List<Cake> getCakes(){
        List<Cake> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    public Optional<Cake> findById(UUID id) {
        return repository.findById(id);
    }

    public Cake createCake(Cake cake) {
        return repository.save(cake);
    }

    public void deleteCake(UUID id) {
        repository.deleteById(id);
    }

    public Cake updateCake(UUID id, Cake cake) {
        if (repository.findById(id).isEmpty()){
            throw new NoSuchElementException("Cake is not found");
        }
        cake.setId(id);
        return repository.save(cake);
    }
}
