package com.greenfox.tribesoflagopus.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @JsonIgnore
  @OneToOne(fetch = FetchType.EAGER)
  private Kingdom kingdom;

  @Builder
  public Location(Integer x, Integer y) {
    this.x = x;
    this.y = y;
  }
}
