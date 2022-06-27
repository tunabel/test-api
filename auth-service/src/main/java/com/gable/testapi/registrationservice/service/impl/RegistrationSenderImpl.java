package com.gable.testapi.registrationservice.service.impl;

import com.gable.testapi.registrationservice.dto.RegistrationDataDto;
import com.gable.testapi.registrationservice.service.RegistrationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Log4j2
@RequiredArgsConstructor
public class RegistrationSenderImpl implements RegistrationSender {

  private final KafkaTemplate<String, RegistrationDataDto> kafkaTemplate;

  @Value(value = "${message.topic.name}")
  private String topicName;
  @Override
  public void sendRegistrationData(RegistrationDataDto dataDto) {
    log.info("Sending registration data for user {}", dataDto.getUsername());
    ListenableFuture<SendResult<String, RegistrationDataDto>> future = kafkaTemplate.send(topicName, dataDto);

    future.addCallback(new ListenableFutureCallback<>() {
      @Override
      public void onSuccess(SendResult<String, RegistrationDataDto> result) {
        log.info("Sent registration data for {} with offset {}", dataDto.toString(),
            result.getRecordMetadata().offset());

      }

      @Override
      public void onFailure(Throwable ex) {
        log.error("Unable to send registration data for user {}", dataDto.getUsername(), ex);
      }
    });
  }

}
