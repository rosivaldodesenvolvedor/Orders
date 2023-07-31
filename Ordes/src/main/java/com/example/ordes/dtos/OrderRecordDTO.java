package com.example.ordes.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderRecordDTO(@NotBlank String description, @NotNull BigDecimal value, @NotEmpty List<String> itemsPedidos) {
}
