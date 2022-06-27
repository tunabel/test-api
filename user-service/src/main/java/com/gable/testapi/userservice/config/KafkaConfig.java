package com.gable.testapi.userservice.config;

import com.gable.testapi.common.dto.RegistrationDataDto;
import com.gable.testapi.common.dto.RegistrationResponseDto;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@EnableKafka
@Configuration
public class KafkaConfig {

  @Value(value = "${kafka.bootstrapAddress}")
  private String bootstrapAddress;


  @Value(value = "${kafka.group.id}")
  private String groupId;

  public ConsumerFactory<String, String> consumerFactory(String groupId) {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 2 * 1024 * 1024);
    props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 2 * 1024 * 1024);
    return new DefaultKafkaConsumerFactory<>(props);
  }

  public ConsumerFactory<String, RegistrationDataDto> registrationConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
        new JsonDeserializer<>(RegistrationDataDto.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, RegistrationDataDto>
  registrationDataConsumerFactory(
      KafkaTemplate<String, RegistrationResponseDto> kafkaTemplate
  ) {

    ConcurrentKafkaListenerContainerFactory<String, RegistrationDataDto> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(registrationConsumerFactory());
    factory.setReplyTemplate(kafkaTemplate);
    return factory;
  }

    @Bean
  public KafkaTemplate<String, RegistrationResponseDto> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public ProducerFactory<String, RegistrationResponseDto> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 2*1024*1024);

    return new DefaultKafkaProducerFactory<>(configProps);
  }
}
