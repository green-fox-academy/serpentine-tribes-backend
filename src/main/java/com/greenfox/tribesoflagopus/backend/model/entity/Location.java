package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private int x;
  private int y;

}
