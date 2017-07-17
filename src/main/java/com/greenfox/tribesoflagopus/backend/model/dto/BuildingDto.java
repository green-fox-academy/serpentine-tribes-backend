package com.greenfox.tribesoflagopus.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.greenfox.tribesoflagopus.backend.model.entity.BuildingType;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"id", "type", "level", "hp"})
public class BuildingDto implements JsonDto {

  @NonNull
  private Long id;
  @NonNull
  private String type;
  @NonNull
  private Integer level;
  private Integer hp;
  private Timestamp startedAt;
  private Timestamp finishedAt;

  @Builder
  public BuildingDto(Long id, BuildingType type, Integer level, Integer hp, Timestamp startedAt,
      Timestamp finishedAt) {
    this.id = id;
    this.type = type.toString();
    this.level = level;
    this.hp = hp;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }
}
