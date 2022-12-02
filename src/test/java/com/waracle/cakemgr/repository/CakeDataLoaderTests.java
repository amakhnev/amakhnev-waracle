package com.waracle.cakemgr.repository;

import com.waracle.cakemgr.entity.Cake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CakeDataLoaderTests {

    @InjectMocks
    CakeDataLoader loader;
    @Mock
    CakeRepository repository;

    @Test
    public void whenInvalidFileNameMentioned_thenExceptionShouldBeThrown() {
        loader.fileName = "someInvalidData.json";
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> loader.loadCakesData(),
                "Expected loadCakesData() to throw exception but it didn't");

        assertEquals("Error occurred when loading data into database.", exception.getMessage());
    }

    @Test
    public void whenInvalidFileContent_thenExceptionShouldBeThrown() {
        loader.fileName = "dataloader/wrongdata.json";
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> loader.loadCakesData(),
                "Expected loadCakesData() to throw exception but it didn't");

        assertEquals("Error occurred when loading data into database.", exception.getMessage());
    }

    @Test
    public void whenFileHasData_thenItShouldBeLoaded() {
        loader.fileName = "dataloader/correctdata.json"; // 3 cakes
        loader.loadCakesData();

        IntStream.range(1, 4)
                .mapToObj(i -> new Cake("Cake " + i + " title"
                        , "Cake " + i + " description"
                        , "Cake " + i + " image URL"))
                .forEach(
                        cake -> Mockito.verify(repository, times(1)).save(cake)
                );

    }

    @Test
    public void whenFileHasDuplicateTitles_thenUniqueShouldBeLoaded() {
        loader.fileName = "dataloader/duplicatesdata.json"; // 6 cakes, duplicated from 3

        //mock repository existsByTitleIgnoreCase responses knowing we will call it twice for each

        for (int i = 1; i <= 3; i++) {
            when(repository.existsByTitleIgnoreCase("Cake " + i + " title"))
                    .thenReturn(false)
                    .thenReturn(true);
        }


        loader.loadCakesData();

        IntStream.range(1, 4)
                .mapToObj(i -> new Cake("Cake " + i + " title"
                        , "Cake " + i + " description"
                        , "Cake " + i + " image URL"))
                .forEach(
                        cake -> Mockito.verify(repository, times(1)).save(cake)
                );


    }

}
