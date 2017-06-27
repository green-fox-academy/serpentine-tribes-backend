package com.greenfox.tribesoflagopus.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonPropertyOrder({"name", "location"})
public class KingdomModifyDto implements JsonDto {

  private String name;
  private LocationDto location;

}