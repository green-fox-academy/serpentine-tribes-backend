package com.greenfox.tribesoflagopus.backend.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginInput implements JsonDto {

  @NotBlank
  private String username;
  @NotBlank
  private String password;
}
