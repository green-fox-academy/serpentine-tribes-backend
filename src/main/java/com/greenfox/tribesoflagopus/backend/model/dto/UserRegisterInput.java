package com.greenfox.tribesoflagopus.backend.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserRegisterInput implements JsonDto {

  @NotBlank
  private String username;
  @NotBlank
  private String password;
  private String kingdom;

}
