package com.greenfox.tribesoflagopus.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by K on 2017.06.16..
 */

@Entity
@Builder
@Data
@AllArgsConstructor
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


  public Resource(String type) {
    this.type = type;
    this.amount = 0;
    this.generation = 0;
  }


}
