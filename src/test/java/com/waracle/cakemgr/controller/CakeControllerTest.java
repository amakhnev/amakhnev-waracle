package com.waracle.cakemgr.controller;

import com.waracle.cakemgr.entity.Cake;

import com.waracle.cakemgr.repository.CakeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;


import java.net.URI;
import java.util.Map;
import java.util.Optional;
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
        assertNotNull(results.getBody());
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

        assertNotNull(actual.getBody());
        // invalid fields will contain error text
        assertNotNull(actual.getBody().getTitle());
        assertNotNull(actual.getBody().getImage());

        // valid fields will be null
        assertNull(actual.getBody().getDescription());

    }

    @Test
    void whenValidCakeForCreation_thenItShouldBeCreated(){
        Cake toCreate = new Cake("Valid title for creation","Valid description","Valid Image URL");
        ResponseEntity<Cake> response = restTemplate.postForEntity(URI.create("/cake"), toCreate, Cake.class);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());

        assertNotNull(response.getBody());
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

    @Test
    void whenInvalidIdReceivedForUpdate_thenBadRequestCodeShouldBeReturned(){
        Cake toUpdate = new Cake("Valid title for update","Valid description","Valid Image URL");
        ResponseEntity<Void> response = restTemplate.exchange(URI.create("/cake/invalid-uuid"), HttpMethod.PUT,new HttpEntity<>(toUpdate, HttpHeaders.EMPTY), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());

    }


    @Test
    void whenNotExistingIdReceivedForUpdate_thenNotFoundCodeShouldBeReturned(){
        Cake toUpdate = new Cake("Valid title for update","Valid description","Valid Image URL");
        ResponseEntity<Void> response = restTemplate.exchange(URI.create("/cake/"+UUID.randomUUID()), HttpMethod.PUT,new HttpEntity<>(toUpdate, HttpHeaders.EMPTY), Void.class);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

    }

    @Test
    void whenInvalidCakeReceivedForUpdate_thenCakeShouldNotBeUpdated(){
        Cake existing = repository.findAll().iterator().next();

        Cake toUpdate = new Cake("","Valid description","");
        ResponseEntity<Cake> response = restTemplate.exchange(URI.create("/cake/"+existing.getId()), HttpMethod.PUT,new HttpEntity<>(toUpdate, HttpHeaders.EMPTY), Cake.class);


        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());

        assertNotNull(response.getBody());
        // invalid fields will contain error text
        assertNotNull(response.getBody().getTitle());
        assertNotNull(response.getBody().getImage());

        // valid fields will be null
        assertNull(response.getBody().getDescription());
    }

    @Test
    void whenValidCakeReceivedForUpdate_thenCakeShouldBeUpdated(){
        UUID existingId = repository.findAll().iterator().next().getId();

        Cake toUpdate = new Cake("Valid title for update","Valid description","Valid Image URL");
        ResponseEntity<Void> response = restTemplate.exchange(URI.create("/cake/"+existingId), HttpMethod.PUT,new HttpEntity<>(toUpdate, HttpHeaders.EMPTY), Void.class);

        assertEquals(HttpStatus.OK,response.getStatusCode());

        Optional<Cake> updated = repository.findById(existingId);
        assertTrue(updated.isPresent());

        assertEquals(existingId,updated.get().getId());
        assertEquals(toUpdate.getTitle(),updated.get().getTitle());
        assertEquals(toUpdate.getDescription(),updated.get().getDescription());
        assertEquals(toUpdate.getImage(),updated.get().getImage());


    }

}