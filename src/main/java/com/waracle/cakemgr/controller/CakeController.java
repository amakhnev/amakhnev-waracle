package com.waracle.cakemgr.controller;

import com.waracle.cakemgr.entity.Cake;
import com.waracle.cakemgr.service.CakeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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

}
