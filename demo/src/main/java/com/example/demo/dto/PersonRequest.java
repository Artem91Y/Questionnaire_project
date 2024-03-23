package com.example.demo.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PersonRequest {

    private String fullName;

    private String email;

    private  String username;
}
