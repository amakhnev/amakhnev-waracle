package com.waracle.cakemgr.service;

import com.waracle.cakemgr.entity.Cake;
import com.waracle.cakemgr.repository.CakeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CakeServiceTest {

    @InjectMocks
    CakeService service;
    @Mock
    CakeRepository repository;


    @Test
    void whenNothingFound_thenEmptyListShouldBeReturned(){
        when(repository.findAll()).thenReturn(new ArrayList<>());

        assertTrue(service.getCakes().isEmpty());
    }


    @Test
    void whenCakeDoesNotExistAndGettingUpdated_thenErrorShouldBeThrown(){
        Cake toUpdate = new Cake("","","");

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                                            ()->service.updateCake(UUID.randomUUID(),toUpdate));

    }


}