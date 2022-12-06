package com.waracle.cakemgr.controller;

import com.waracle.cakemgr.entity.Cake;

import com.waracle.cakemgr.repository.CakeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


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
    void whenInvalidParameterReceivedForGet_thenBadRequestCodeShouldBeReturned() {

        ResponseEntity<Cake> actual = restTemplate.getForEntity(URI.create("/cake/not-valid-uuid"), Cake.class);

        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());

    }


    @Test
    void whenInvalidCakeForCreation_thenItShouldReturnValidationError(){
        Cake toCreate = new Cake("","Valid description","");
        ResponseEntity<Cake> actual = restTemplate.postForEntity(URI.create("/cake"), toCreate, Cake.class);

        assertEquals(HttpStatus.BAD_REQUEST,actual.getStatusCode());
        // invalid fields will contain error text
        assertNotNull(actual.getBody().getTitle());
        assertNotNull(actual.getBody().getImage());

        // valid fields will be null
        assertNull(actual.getBody().getDescription());

    }

    @Test
    void whenValidCakeForCreation_thenItShouldBeCreated(){
        Cake toCreate = new Cake("Valid title","Valid description","Valid Image URL");
        ResponseEntity<Cake> response = restTemplate.postForEntity(URI.create("/cake"), toCreate, Cake.class);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        Cake actual = response.getBody();

        assertNotNull(actual.getId());
        assertEquals(toCreate.getTitle(),actual.getTitle());
        assertEquals(toCreate.getDescription(),actual.getDescription());
        assertEquals(toCreate.getImage(),actual.getImage());

    }

    @Test
    void whenInvalidParameterReceivedForDelete_thenBadRequestCodeShouldBeReturned() {

        ResponseEntity<Void> response =
            restTemplate.exchange(URI.create("/cake/not-valid-uuid"), HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());

    }

    @Test
    void whenNotExistingUUIDReceivedForDelete_thenNotFoundCodeShouldBeReturned() {

        ResponseEntity<Void> response =
                restTemplate.exchange(URI.create("/cake/"+UUID.randomUUID()), HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

    }

    @Test
    void whenValidIdReceivedForDeletion_thenCakeShouldBeDeleted(){
        Cake toDelete = repository.findAll().iterator().next();
        ResponseEntity<Void> response =
                restTemplate.exchange(URI.create("/cake/"+toDelete.getId()), HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());

        assertTrue(repository.findById(toDelete.getId()).isEmpty());

    }
}