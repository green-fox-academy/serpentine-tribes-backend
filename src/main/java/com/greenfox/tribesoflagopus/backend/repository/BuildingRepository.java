package com.greenfox.tribesoflagopus.backend.repository;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import org.springframework.data.repository.CrudRepository;

public interface BuildingRepository extends CrudRepository<Building, Long> {

  boolean existsByTypeAndKingdomId(String buildingType, long kingdomId);

  Building findTopByKingdomAndTypeOrderByIdDesc(Kingdom kingdom, String type);

  Building findById(Long id);

  boolean existsByIdAndKingdomUserId (Long buildingId, Long userId);
}
