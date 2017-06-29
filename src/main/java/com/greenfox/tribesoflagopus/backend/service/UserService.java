package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public boolean existsUserById(Long userId) {
    return userRepository.exists(userId);
  }

  public boolean existsUserByUsername(String username){
    return  userRepository.existsByUsername(username);
  }
}
