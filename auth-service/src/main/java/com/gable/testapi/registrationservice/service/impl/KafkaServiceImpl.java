package com.gable.testapi.registrationservice.service.impl;

import com.gable.testapi.common.dto.RegistrationDataDto;
import com.gable.testapi.common.dto.RegistrationResponseDto;
import com.gable.testapi.registrationservice.exception.ServerInternalException;
import com.gable.testapi.registrationservice.service.KafkaService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {


  @Value(value = "${kafka.topic.request}")
  private String requestTopic;

  private final ReplyingKafkaTemplate<String, RegistrationDataDto, RegistrationResponseDto> kafkaTemplate;

  @Override
  public RegistrationResponseDto sendRegistrationData(RegistrationDataDto dataDto) {
    log.info("Sending registration data for user {}", dataDto.getUsername());

    ProducerRecord<String, RegistrationDataDto> producerRecord = new ProducerRecord<>(requestTopic, dataDto);
    RequestReplyFuture<String, RegistrationDataDto, RegistrationResponseDto> replyFuture = kafkaTemplate.sendAndReceive(producerRecord);
    try {
      SendResult<String, RegistrationDataDto> sendResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
      ConsumerRecord<String, RegistrationResponseDto> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);
      RegistrationResponseDto response = consumerRecord.value();
      log.info("Kafka response: {}", response.toString());
      return response;
    } catch (InterruptedException | ExecutionException | RuntimeException | TimeoutException e) {
      log.error("Error sending registration data", e);
      throw new ServerInternalException(e.getMessage());
    }

  }

}
