package com.gable.testapi.registrationservice.service;

import com.gable.testapi.registrationservice.dto.RegistrationRequestDto;

public interface RegistrationService {

  void sendRegistrationData(RegistrationRequestDto requestDto);
}
