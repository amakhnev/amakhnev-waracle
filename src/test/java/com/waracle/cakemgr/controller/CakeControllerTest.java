package com.waracle.cakemgr.controller;

import com.waracle.cakemgr.entity.Cake;

import com.waracle.cakemgr.repository.CakeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CakeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CakeRepository repository;

    @Test
    void whenSomeCakesFound_thenListIsReturned() {

        ResponseEntity<Cake[]> results = restTemplate.getForEntity(URI.create("/cake"), Cake[].class);

        assertEquals(HttpStatus.OK, results.getStatusCode());
        assertEquals(3, results.getBody().length);

    }

    @Test
    void whenCakeExists_thenItShouldBeFound() {
        Cake cake = repository.findAll().iterator().next();

        ResponseEntity<Cake> actual = restTemplate.getForEntity(URI.create("/cake/" + cake.getId()), Cake.class);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(cake, actual.getBody());

    }

    @Test
    void whenCakeNosNotExists_thenItShouldNotBeFound() {

        ResponseEntity<Cake> actual = restTemplate.getForEntity(URI.create("/cake/"+ UUID.randomUUID()), Cake.class);

        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());

    }

    @Test
    void whenInvalidParameterReceived_thenBadRequestCodeShouldBeReturned() {

        ResponseEntity<Cake> actual = restTemplate.getForEntity(URI.create("/cake/not-valid-uuid"), Cake.class);

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());

    }
}