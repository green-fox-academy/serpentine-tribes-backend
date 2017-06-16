package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by K on 2017.06.16..
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

  private int x;
  private int y;

}
