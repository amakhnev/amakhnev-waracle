package com.waracle.cakemgr.controller;

import com.waracle.cakemgr.entity.Cake;
import com.waracle.cakemgr.service.CakeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CakeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CakeController.class);

    @Autowired
    private final CakeService service;

    public CakeController(CakeService service) {
        this.service = service;
    }

    @GetMapping("/cakes")
    public List<Cake> getCakes(){
        LOGGER.info("received /cakes post request");
        return service.getCakes();
    }

}
