package com.gable.testapi.userservice.service.impl;

import com.gable.testapi.common.dto.RegistrationDataDto;
import com.gable.testapi.userservice.exception.InvalidRequestException;
import com.gable.testapi.userservice.model.User;
import com.gable.testapi.userservice.repository.UserRepository;
import com.gable.testapi.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Value("${app.encryption.secretkey}")
  private String secretKey;

  @Override
  public long saveUser(RegistrationDataDto registrationDataDto) {

    userRepository.findByUsername(registrationDataDto.getUsername())
        .ifPresent(user -> {
          throw new InvalidRequestException(
              "Username existed: " + registrationDataDto.getUsername());
        });

    User user = new User();
    user.setUsername(registrationDataDto.getUsername());
    user.setPassword(SimpleEncryptionService.encrypt(registrationDataDto.getPassword(), secretKey));
    user.setMemberType(registrationDataDto.getMemberType());
    user.setSalary(registrationDataDto.getSalary());

    return userRepository.save(user).getUserId();
  }
}
