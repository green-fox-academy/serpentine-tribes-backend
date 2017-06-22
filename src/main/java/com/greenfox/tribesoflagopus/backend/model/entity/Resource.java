package com.greenfox.tribesoflagopus.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Resource {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String type;
  private int amount;
  private int generation;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.EAGER)
  private Kingdom kingdom;

  @Builder
  public Resource(String type, int amount, int generation,
          Kingdom kingdom) {
    this.type = type;
    this.amount = amount;
    this.generation = generation;
    this.kingdom = kingdom;
  }

  public Resource(String type) {
    this.type = type;
    this.amount = 0;
    this.generation = 0;
  }
}
