package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.Entity;
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

  private String type;
  private int amount;
  private int generation;

  public Resource(String type) {
    this.type = type;
    this.amount = 0;
    this.generation = 0;
  }


}
