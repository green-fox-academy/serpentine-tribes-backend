package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "seq_store", sequenceName = "user_sequence")
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_store")
  private Long id;

  private String username;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Kingdom kingdom;

  private String password;

  private String avatar;
  private long points;

  private String token;

  @Builder
  public User(String username, String password, String avatar, long points) {
    this.username = username;
    this.password = password;
    this.avatar = avatar;
    this.points = points;
  }
}
