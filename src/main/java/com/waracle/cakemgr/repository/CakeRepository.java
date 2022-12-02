package com.waracle.cakemgr.repository;

import com.waracle.cakemgr.entity.Cake;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CakeRepository extends CrudRepository<Cake, UUID> {



}
