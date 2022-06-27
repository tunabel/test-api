package com.gable.testapi.registrationservice.config;


import com.gable.testapi.common.dto.RegistrationDataDto;
import com.gable.testapi.common.dto.RegistrationResponseDto;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {

  @Value(value = "${kafka.bootstrapAddress}")
  private String bootstrapAddress;

  @Value(value = "${kafka.consumer.groupid}")
  private String consumerGroup;

  @Value(value = "${kafka.topic.response}")
  private String responseTopic;

  @Bean
  public ProducerFactory<String, RegistrationDataDto> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 2 * 1024 * 1024);

    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public ReplyingKafkaTemplate<String, RegistrationDataDto, RegistrationResponseDto> replyingTemplate(
      ProducerFactory<String, RegistrationDataDto> producerFactory,
      ConcurrentMessageListenerContainer<String, RegistrationResponseDto> repliesContainer) {
    ReplyingKafkaTemplate<String, RegistrationDataDto, RegistrationResponseDto> replyTemplate =
        new ReplyingKafkaTemplate<>(producerFactory, repliesContainer);
    replyTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
    replyTemplate.setSharedReplyTopic(true);
    return replyTemplate;
  }

  public ConsumerFactory<String, RegistrationResponseDto> registrationConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
//    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//    props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, JsonDeserializer.class);
//    props.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
//    props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
//    props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserRegResponseDto.class);
//    props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.gable");
    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
        new JsonDeserializer<>(RegistrationResponseDto.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, RegistrationResponseDto> containerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, RegistrationResponseDto> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(registrationConsumerFactory());
    return factory;
  }

  @Bean
  public ConcurrentMessageListenerContainer<String, RegistrationResponseDto> repliesContainer(
      ConcurrentKafkaListenerContainerFactory<String, RegistrationResponseDto> containerFactory) {
    ConcurrentMessageListenerContainer<String, RegistrationResponseDto> repliesContainer = containerFactory.createContainer(
        responseTopic);
    repliesContainer.getContainerProperties().setGroupId(consumerGroup);
    repliesContainer.setAutoStartup(false);
    return repliesContainer;
  }

}
