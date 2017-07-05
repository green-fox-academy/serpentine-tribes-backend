package com.greenfox.tribesoflagopus.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@JsonPropertyOrder({"name", "location"})
public class KingdomInputModifyDto implements JsonDto {

  private String name;
  private LocationDto location;

}