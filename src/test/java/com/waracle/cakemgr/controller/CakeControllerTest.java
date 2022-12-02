package com.waracle.cakemgr.controller;

import com.waracle.cakemgr.entity.Cake;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CakeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void whenSomeCakesFound_thenListIsReturned() {

        ResponseEntity<Cake[]> results = restTemplate.getForEntity(URI.create("/cakes"), Cake[].class);

        assertEquals(HttpStatus.OK,results.getStatusCode());
        assertEquals(3, results.getBody().length);

    }


}