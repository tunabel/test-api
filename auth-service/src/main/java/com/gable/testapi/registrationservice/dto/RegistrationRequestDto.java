package com.gable.testapi.registrationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gable.testapi.registrationservice.constant.Constants;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequestDto {

  @NotBlank
  @Pattern(regexp = Constants.EMAIL_REGEX, message = "invalid email format")
  private String email;

  @NotBlank
  @Min(8)
  private String password;

  @NotNull
  @Min(15000)
  @JsonProperty("based_salary")
  private double salary;

}
