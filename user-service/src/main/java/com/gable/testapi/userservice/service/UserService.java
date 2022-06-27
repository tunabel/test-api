package com.gable.testapi.userservice.service;

import com.gable.testapi.common.dto.RegistrationDataDto;

public interface UserService {

  long saveUser(RegistrationDataDto registrationDataDto);
}
