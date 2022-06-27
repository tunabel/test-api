package com.gable.testapi.userservice.service.impl;

import com.gable.testapi.common.dto.RegistrationDataDto;
import com.gable.testapi.common.dto.RegistrationResponseDto;
import com.gable.testapi.userservice.service.KafkaService;
import com.gable.testapi.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaServiceImpl implements KafkaService {

  private final UserService userService;

  @KafkaListener(
      topics = "${kafka.topic.request}",
      containerFactory = "registrationDataConsumerFactory")
  @SendTo
  public Message<RegistrationResponseDto> registrationDataListener(RegistrationDataDto dataDto) {
    log.info("Received registration data for user {}", dataDto.toString());

    try {

      long createdUserId = userService.saveUser(dataDto);
      log.info("Created Uesr {} with id {}", dataDto.getUsername(), createdUserId);
      return MessageBuilder.withPayload(new RegistrationResponseDto(createdUserId, null)).build();
    } catch (Exception ex) {
      log.error("Error creating user {}", dataDto.getUsername(), ex);
      return MessageBuilder.withPayload(new RegistrationResponseDto(null, ex.getMessage())).build();
    }

  }

}
