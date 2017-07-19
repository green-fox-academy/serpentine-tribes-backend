package com.greenfox.tribesoflagopus.backend.model.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "seq_store", sequenceName = "location_sequence")
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_store")
  private Long id;
  private Integer x;
  private Integer y;

  @OneToOne(fetch = FetchType.LAZY)
  @NotNull
  private Kingdom kingdom;

  @Builder
  public Location(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
