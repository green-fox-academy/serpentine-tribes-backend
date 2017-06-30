package com.greenfox.tribesoflagopus.backend.service;

import com.greenfox.tribesoflagopus.backend.model.entity.User;
import com.greenfox.tribesoflagopus.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;

@Service
public class TokenService {

  @Autowired
  UserRepository userRepository;

  private Key key;

  public void saveTokenToUser(User user){
    userRepository.findByUsername(user.getUsername()).setToken(generateToken(user));
    userRepository.save(user);
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


  public long getIdFromToken(String token) {
    long id;
      Claims claims = getClaimsFromToken(token);
      id = (Integer) claims.get("id");
    return id;
  }

  public User getUserFromToken(String token) {
    long idFromToken = getIdFromToken(token);
    User userFromToken = userRepository.findOne(idFromToken);
    return userFromToken;
  }
}
