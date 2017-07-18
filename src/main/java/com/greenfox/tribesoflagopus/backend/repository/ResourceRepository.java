package com.greenfox.tribesoflagopus.backend.repository;

import com.greenfox.tribesoflagopus.backend.model.entity.Building;
import com.greenfox.tribesoflagopus.backend.model.entity.Resource;
import com.greenfox.tribesoflagopus.backend.model.entity.ResourceType;
import org.springframework.data.repository.CrudRepository;

public interface ResourceRepository extends CrudRepository <Resource, Long> {

  Resource findByTypeAndKingdomId (ResourceType resourceType, long kingdomId);
}
