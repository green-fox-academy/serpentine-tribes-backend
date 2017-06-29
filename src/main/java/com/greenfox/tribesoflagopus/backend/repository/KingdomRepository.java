package com.greenfox.tribesoflagopus.backend.repository;

import com.greenfox.tribesoflagopus.backend.model.entity.Kingdom;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface KingdomRepository extends CrudRepository<Kingdom, Long> {

  @Override
  List<Kingdom> findAll();

  Kingdom findOneByUserId(Long userId);

  boolean existsById(long id);

}
