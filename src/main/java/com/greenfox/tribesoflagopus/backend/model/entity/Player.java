package com.greenfox.tribesoflagopus.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfox.tribesoflagopus.backend.model.dto.JsonDto;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player implements JsonDto {

  private long id;
  private String username;
  private long kingdomId;

  @JsonIgnore
  private String password;

}
