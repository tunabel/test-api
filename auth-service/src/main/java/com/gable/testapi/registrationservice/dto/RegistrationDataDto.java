package com.gable.testapi.registrationservice.dto;

import com.gable.testapi.registrationservice.constant.MemberType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDataDto {

  private String username;
  private String password;
  private double salary;
  private MemberType memberType;

}
