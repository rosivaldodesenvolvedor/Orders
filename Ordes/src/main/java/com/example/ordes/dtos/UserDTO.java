package com.example.ordes.dtos;

import com.example.ordes.enums.UserRole;

public record UserDTO(String userName, String password, UserRole role) {
}