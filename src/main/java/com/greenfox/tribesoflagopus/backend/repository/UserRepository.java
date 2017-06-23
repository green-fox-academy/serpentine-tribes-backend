package com.greenfox.tribesoflagopus.backend.repository;

import com.greenfox.tribesoflagopus.backend.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

}