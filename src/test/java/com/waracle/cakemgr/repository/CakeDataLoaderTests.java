package com.waracle.cakemgr.repository;

import com.waracle.cakemgr.entity.Cake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class CakeDataLoaderTests {

    CakeDataLoader loader;
    CakeRepository repository;


    @BeforeEach
    void setUp(){
        repository = mock(CakeRepository.class);
        loader = new CakeDataLoader(repository);
    }

    @Test
    public void whenInvalidFileNameMentioned_thenExceptionShouldBeThrown(){
        loader.fileName = "someInvalidData.json";
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                ()->loader.loadCakesData(),
                "Expected loadCakesData() to throw exception but it didn't");

        assertEquals("Error occurred when loading data into database.",exception.getMessage());
    }

    @Test
    public void whenInvalidFileContent_thenExceptionShouldBeThrown(){
        loader.fileName = "dataloader/wrongdata.json";
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                ()->loader.loadCakesData(),
                "Expected loadCakesData() to throw exception but it didn't");

        assertEquals("Error occurred when loading data into database.",exception.getMessage());
    }

    @Test
    public void whenFileHasData_thenItShouldBeLoaded(){
        loader.fileName = "dataloader/correctdata.json"; // 3 cakes
        loader.loadCakesData();
        List<Cake> cakes =
        IntStream.range(1,4)
                .mapToObj( i -> new Cake("Cake "+i+" title"
                                    ,"Cake "+i+" description"
                                    ,"Cake "+i+" image URL"))
                .collect(Collectors.toList());

        Mockito.verify(repository,times(1)).saveAll(cakes);

    }

    @Test
    public void whenFileHasDuplicates_thenUniqueShouldBeLoaded(){
        loader.fileName = "dataloader/duplicatesdata.json"; // 6 cakes, duplicated from 3
        loader.loadCakesData();
        List<Cake> cakes =
                IntStream.range(1,4)
                        .mapToObj( i -> new Cake("Cake "+i+" title"
                                ,"Cake "+i+" description"
                                ,"Cake "+i+" image URL"))
                        .collect(Collectors.toList());

        Mockito.verify(repository,times(1)).saveAll(cakes);

    }

}
