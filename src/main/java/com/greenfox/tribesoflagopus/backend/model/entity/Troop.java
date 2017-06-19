package com.greenfox.tribesoflagopus.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@AllArgsConstructor
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

  public Troop(int level, int hp, int attack, int defence){
    this.level=1;
    this.hp=hp;
    this.attack=attack;
    this.defence=defence;
  }
}
