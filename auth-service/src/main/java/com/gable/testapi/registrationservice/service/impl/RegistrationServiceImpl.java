package com.gable.testapi.registrationservice.service.impl;

import com.gable.testapi.common.dto.RegistrationDataDto;
import com.gable.testapi.common.constants.enums.MemberType;
import com.gable.testapi.common.dto.RegistrationResponseDto;
import com.gable.testapi.registrationservice.dto.RegistrationRequestDto;
import com.gable.testapi.registrationservice.dto.CreatedUserDto;
import com.gable.testapi.registrationservice.exception.ServerInternalException;
import com.gable.testapi.registrationservice.service.KafkaService;
import com.gable.testapi.registrationservice.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

  private final KafkaService kafkaService;

  @Override
  public CreatedUserDto createUserWithRequestData(RegistrationRequestDto requestDto) {
    RegistrationResponseDto responseDto = kafkaService.sendRegistrationData(prepareRegistrationData(requestDto));

    if (responseDto.getError() != null) {
      throw new ServerInternalException("Error registering data. Cause: " + responseDto.getError());
    }
    return new CreatedUserDto(responseDto.getUserId());
  }

  public RegistrationDataDto prepareRegistrationData(RegistrationRequestDto requestDto) {

    RegistrationDataDto dataDto = new RegistrationDataDto();
    dataDto.setUsername(requestDto.getEmail());
    dataDto.setPassword(requestDto.getPassword());
    dataDto.setSalary(requestDto.getSalary());

    MemberType memberType;
    if (requestDto.getSalary() <= 30000) {
      memberType = MemberType.SILVER;
    } else if (requestDto.getSalary() <= 50000) {
      memberType = MemberType.GOLD;
    } else {
      memberType = MemberType.PLATINUM;
    }

    dataDto.setMemberType(memberType);

    return dataDto;
  }
}
