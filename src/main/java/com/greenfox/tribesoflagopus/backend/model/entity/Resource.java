package com.greenfox.tribesoflagopus.backend.model.entity;

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
public class Resource {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String type;
  private int amount;
  private int generation;

  @ManyToOne(fetch = FetchType.EAGER)
  private Kingdom kingdom;

  @Builder
  public Resource(String type, int amount, int generation) {
    this.type = type;
    this.amount = amount;
    this.generation = generation;
  }

  public Resource(String type) {
    this.type = type;
  }
}
