package com.greenfox.tribesoflagopus.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Troop {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.EAGER)
  private Kingdom kingdom;

  private int level;
  private int hp;
  private int attack;
  private int defence;

  @Builder
  public Troop(int level, int hp, int attack, int defence) {
    this.level = 1;
    this.hp = hp;
    this.attack = attack;
    this.defence = defence;
  }
}
