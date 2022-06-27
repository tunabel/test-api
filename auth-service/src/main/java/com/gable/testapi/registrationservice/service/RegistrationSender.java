package com.gable.testapi.registrationservice.service;

import com.gable.testapi.registrationservice.dto.RegistrationDataDto;

public interface RegistrationSender {

  void sendRegistrationData(RegistrationDataDto dataDto);
}
