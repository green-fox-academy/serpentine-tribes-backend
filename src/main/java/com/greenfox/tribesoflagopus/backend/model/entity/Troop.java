package com.greenfox.tribesoflagopus.backend.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Troop {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotNull(message = "What is the level of the troop?")
  private int level;

  @NotNull(message = "What is the hp of the troop?")
  private int hp;

  @NotNull(message = "What is the attack value of the troop?")
  private int attack;

  @NotNull(message = "What is the defence value of the troop?")
  private int defence;

  public Troop(){
  }

  public Troop(int level, int hp, int attack, int defence){
    this.level=1;
    this.hp=hp;
    this.attack=attack;
    this.defence=defence;
  }
}
