package com.greenfox.tribesoflagopus.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.greenfox.tribesoflagopus.backend.model.entity.ResourceType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"id", "level", "hp", "attack", "defence"})
public class ResourceDto {

  @NonNull
  private String type;
  private Integer amount;
  private Integer generation;

  @Builder
  public ResourceDto(ResourceType type, Integer amount, Integer generation) {
    this.type = type.toString();
    this.amount = amount;
    this.generation = generation;
  }
}
