package com.gable.testapi.registrationservice.service;


import com.gable.testapi.common.dto.RegistrationDataDto;
import com.gable.testapi.common.dto.RegistrationResponseDto;

public interface KafkaService {

  RegistrationResponseDto sendRegistrationData(RegistrationDataDto dataDto);
}
