package com.waracle.cakemgr.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waracle.cakemgr.entity.Cake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class CakeDataLoader {
    private static final Logger LOG = LoggerFactory.getLogger(CakeDataLoader.class);

    @Autowired
    CakeRepository repository;

    @Value("${com.waracle.cakemgr.dataloader.filename}") protected String fileName;

    public CakeDataLoader(CakeRepository repository){
        this.repository = repository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadCakesData(){
        LOG.info("Loading seed data into database");
        ClassLoader classLoader = getClass().getClassLoader();
        Type setType = new TypeToken<Set<Cake>>(){}.getType();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            if (inputStream == null){
                throw new IllegalArgumentException("Wrong file to load: "+fileName);
            }
            Set<Cake> cakes = (new Gson()).fromJson(new InputStreamReader(inputStream), setType);
            repository.saveAll(new ArrayList<>(cakes));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new IllegalStateException("Error occurred when loading data into database.", e);
        }
        LOG.info("Seed data successfully loaded");
    }


}
