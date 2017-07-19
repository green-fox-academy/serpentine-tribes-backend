package com.greenfox.tribesoflagopus.backend.model.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "seq_store", sequenceName = "building_sequence")
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class Building {

  public static final Timestamp ZERO_TIMESTAMP = new Timestamp(0);

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_store")
  private Long id;

  @Enumerated(EnumType.STRING)
  private BuildingType type;
  private int level;
  private int hp;
  private Timestamp startedAt;

  @Transient
  private Timestamp finishedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Kingdom kingdom;

  @Builder
  public Building(String type, Timestamp startedAt) {
    this.type = BuildingType.getByName(type);
    this.level = 1;
    this.hp = 0;
    this.startedAt = startedAt;
  }

  public static class BuildingBuilder {
    private int level = 1;
  }

  public boolean isFinished () {
    if (finishedAt == null || ZERO_TIMESTAMP.equals(finishedAt)) {
      return false;
    }
    return true;
  }
}