package com.greenfox.tribesoflagopus.backend.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Troop {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private int level;
  private int hp;
  private int attack;
  private int defence;

  public Troop(){
  }

  @Builder
  public Troop(int level, int hp, int attack, int defence){
    this.level=1;
    this.hp=hp;
    this.attack=attack;
    this.defence=defence;
  }
}
