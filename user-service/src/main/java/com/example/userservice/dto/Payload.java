package com.example.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Payload {
    private String user_id;
    private String pwd;
    private String name;
    private String created_at;
    private String modified_at;
}
