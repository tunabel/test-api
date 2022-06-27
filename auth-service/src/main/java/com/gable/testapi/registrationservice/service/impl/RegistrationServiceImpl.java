package com.gable.testapi.registrationservice.service.impl;

import com.gable.testapi.registrationservice.constant.MemberType;
import com.gable.testapi.registrationservice.dto.RegistrationDataDto;
import com.gable.testapi.registrationservice.dto.RegistrationRequestDto;
import com.gable.testapi.registrationservice.service.RegistrationSender;
import com.gable.testapi.registrationservice.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

  private final RegistrationSender registrationSender;

  @Override
  public void sendRegistrationData(RegistrationRequestDto requestDto) {
    registrationSender.sendRegistrationData(prepareRegistrationData(requestDto));
  }

  private RegistrationDataDto prepareRegistrationData(RegistrationRequestDto requestDto) {

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
