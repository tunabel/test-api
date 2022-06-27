package com.gable.testapi.registrationservice.service;

import com.gable.testapi.registrationservice.dto.RegistrationRequestDto;
import com.gable.testapi.registrationservice.dto.CreatedUserDto;

public interface RegistrationService {

  CreatedUserDto createUserWithRequestData(RegistrationRequestDto requestDto);
}
