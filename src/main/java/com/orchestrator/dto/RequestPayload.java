package com.orchestrator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestPayload {
    @NotBlank
    @JsonProperty("Name")
    private String name;

    @NotBlank
    @JsonProperty("Surname")
    private String surName;
}
