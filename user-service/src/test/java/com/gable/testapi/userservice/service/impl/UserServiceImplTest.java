package com.gable.testapi.userservice.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gable.testapi.common.constants.enums.MemberType;
import com.gable.testapi.common.dto.RegistrationDataDto;
import com.gable.testapi.userservice.exception.InvalidRequestException;
import com.gable.testapi.userservice.model.User;
import com.gable.testapi.userservice.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @InjectMocks
  UserServiceImpl userService;

  @Mock
  UserRepository userRepository;

  @Mock
  RegistrationDataDto registrationDataDto = new RegistrationDataDto();

  @BeforeEach
  public void setup() {

    registrationDataDto.setUsername("email@test.com");
    registrationDataDto.setPassword("12345678");
    registrationDataDto.setMemberType(MemberType.PLATINUM);
    registrationDataDto.setSalary(100000.0);
  }

  @Test
  void saveUser_willThrowException_whenUsernameExists() {
    when(userRepository.findByUsername(registrationDataDto.getUsername()))
        .thenReturn(Optional.of(new User()));

    Assertions.assertThrows(
        InvalidRequestException.class,
        () -> userService.saveUser(registrationDataDto));
  }

  @Test
  void saveUser_willSaveUser() {

    User newUser = new User();
    newUser.setUserId(1L);
    when(userRepository.save(any())).thenReturn(newUser);

    long createdId = userService.saveUser(registrationDataDto);
    verify(userRepository, Mockito.atLeastOnce()).save(any(User.class));
    Assertions.assertEquals(1L, createdId);
  }
}