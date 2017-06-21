package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by K on 2017.06.16..
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private Integer x;
  private Integer y;

}
