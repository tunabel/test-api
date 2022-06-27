package com.gable.testapi.registrationservice.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gable.testapi.common.constants.enums.MemberType;
import com.gable.testapi.common.dto.RegistrationDataDto;
import com.gable.testapi.common.dto.RegistrationResponseDto;
import com.gable.testapi.registrationservice.dto.CreatedUserDto;
import com.gable.testapi.registrationservice.dto.RegistrationRequestDto;
import com.gable.testapi.registrationservice.exception.ServerInternalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

  @InjectMocks
  RegistrationServiceImpl registrationService;

  @Mock
  KafkaServiceImpl kafkaService;

  RegistrationRequestDto requestDto = new RegistrationRequestDto();
  @BeforeEach
  public void setup() {
    requestDto.setPassword("12345678");
    requestDto.setEmail("test@email.com");
    requestDto.setSalary(100000.0);
  }

  @Test
  void createUserWithRequestData_returnError_willThrowException() {
    RegistrationResponseDto responseDto = new RegistrationResponseDto(null, "msg");
    when(kafkaService.sendRegistrationData(any())).thenReturn(responseDto);

    Assertions.assertThrows(ServerInternalException.class,
        () -> registrationService.createUserWithRequestData(requestDto));
  }

  @Test
  void createUserWithRequestData_willReturnCreatedUserId() {
    RegistrationResponseDto responseDto = new RegistrationResponseDto(1L, null);
    when(kafkaService.sendRegistrationData(any())).thenReturn(responseDto);

    CreatedUserDto createdUserDto = registrationService.createUserWithRequestData(requestDto);
    Assertions.assertEquals(1L, createdUserDto.getCreatedUserId());
  }

  @Test
  void prepareRegistrationData() {
    requestDto.setSalary(18000.0);
    RegistrationDataDto registrationDataDto = registrationService.prepareRegistrationData(requestDto);

    Assertions.assertEquals(MemberType.SILVER, registrationDataDto.getMemberType());
  }
}