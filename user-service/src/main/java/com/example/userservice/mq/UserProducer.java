package com.example.userservice.mq;

import com.example.userservice.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserProducer {
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public UserProducer(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    List<Field> fields = Arrays.asList(
            new Field("string", true, "user_id"),
            new Field("string", true, "pwd"),
            new Field("string", true, "name"),
            new Field("string", true, "created_at"),
            new Field("string", true, "modified_at")
    );

    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("users")
            .build();

    public UserDto send(String topic, UserDto userDto){
        Payload payload = Payload.builder()
        .user_id(userDto.getUserId())
                .pwd(userDto.getPwd())
                .name(userDto.getName())
                .created_at("2021-08-24")
                .modified_at("2021-08-24")
                .build();

        KafkaUserDto kafkaUserDto = new KafkaUserDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try{
            jsonInString = mapper.writeValueAsString(kafkaUserDto);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Order Producer send data from the order-service" + kafkaUserDto);

        return userDto;
    }
}
