package com.greenfox.tribesoflagopus.backend.repository;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import org.springframework.data.repository.CrudRepository;

public interface BuildingRepository extends CrudRepository<Building, Long> {

  boolean existsByTypeAndKingdomId(BuildingType buildingType, long kingdomId);

  Building findById(Long id);

  boolean existsByIdAndKingdomUserId (Long buildingId, Long userId);
}
