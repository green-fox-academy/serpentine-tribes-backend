package com.greenfox.tribesoflagopus.backend.repository;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import org.springframework.data.repository.CrudRepository;

public interface BuildingRepository extends CrudRepository<Building, Long> {

  boolean existsByTypeAndKingdomId(String buildingType, long kingdomId);
}
