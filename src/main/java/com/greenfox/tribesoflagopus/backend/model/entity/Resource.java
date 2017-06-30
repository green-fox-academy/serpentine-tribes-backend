package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "seq_store", sequenceName = "resource_sequence")
@Getter
@Setter
@NoArgsConstructor
public class Resource {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_store")
  private Long id;

  private String type;
  private int amount;
  private int generation;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
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
