package com.example.restaurant.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class UserDTO {

    private Long id;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    private String username;
}
