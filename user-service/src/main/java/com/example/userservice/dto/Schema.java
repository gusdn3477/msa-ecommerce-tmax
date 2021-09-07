package com.example.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Schema {
    private String type;
    private List<Field> fields;
    private Boolean optional;
    private String name;
}
