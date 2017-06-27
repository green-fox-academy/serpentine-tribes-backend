package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String username;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Kingdom kingdom;

  private String password;

  private String avatar;
  private long points;

  @Builder
  public User(String username, String password, String avatar, long points) {
    this.username = username;
    this.password = password;
    this.avatar = avatar;
    this.points = points;
  }
}
