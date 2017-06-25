package com.greenfox.tribesoflagopus.backend.repository;

import com.greenfox.tribesoflagopus.backend.model.entity.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {

  boolean existsByXAndY(Integer x, Integer y);
}
