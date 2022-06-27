package com.gable.testapi.common.dto;

import com.gable.testapi.common.constants.enums.MemberType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegistrationDataDto {

  private String username;
  private String password;
  private double salary;
  private MemberType memberType;

}
