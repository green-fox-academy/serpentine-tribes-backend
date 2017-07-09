package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.User;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;

@Service
public class TokenService {

  private final UserRepository userRepository;

  private Key key;

  @Autowired
  public TokenService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public String saveNewTokenToUser(String username){
    User existingUser = userRepository.findByUsername(username);
    String token = generateToken(existingUser);
    existingUser.setToken(token);
    userRepository.save(existingUser);
    return token;
  }

  public String generateToken(User user) {
    HashMap<String, Object> claims = new HashMap<>();
    claims.put("id", user.getId());
    claims.put("username", user.getUsername());
    String token = generateTokenByClaims(claims);
    return token;
  }

  private String generateTokenByClaims(HashMap<String, Object> claims) {
    this.key = MacProvider.generateKey();
    String token = Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, key)
            .compact();
    return token;
  }

  private Claims getClaimsFromToken(String token) {
    Claims claims;
    claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();
    return claims;
  }


  public Long getIdFromToken(String token) {
    Long id;
    try {
      Claims claims = getClaimsFromToken(token);
      Integer recoveredId = (Integer) claims.get("id");
      id = recoveredId.longValue();
    } catch (Exception e) {
      id = null;
    }
    return id;
  }

  public User getUserFromToken(String token) {
    long idFromToken = getIdFromToken(token);
    User userFromToken = userRepository.findOne(idFromToken);
    return userFromToken;
  }
}
