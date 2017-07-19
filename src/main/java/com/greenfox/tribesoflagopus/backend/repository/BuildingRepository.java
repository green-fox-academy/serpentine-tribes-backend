package com.greenfox.tribesoflagopus.backend.repository;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BuildingRepository extends CrudRepository<Building, Long> {

  boolean existsByTypeAndKingdomId(BuildingType buildingType, long kingdomId);

  boolean existsByIdAndKingdomUserId (Long buildingId, Long userId);

  List<Building> findAllByKingdomUserId(Long userId);

  Building findByTypeAndKingdomId(BuildingType buildingType, long kingdomId);
}
