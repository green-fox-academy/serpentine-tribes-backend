package com.greenfox.tribesoflagopus.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonPropertyOrder({"id", "level", "hp", "attack", "defence"})
public class ResourceDto {

  @NonNull
  private String type;
  private Integer amount;
  private Integer generation;

}
