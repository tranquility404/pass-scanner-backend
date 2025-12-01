package com.hackathon.gatepass.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePassRequest {

    @NotBlank(message = "Pass code is required")
    private String passCode;

    @NotBlank(message = "Team name is required")
    private String teamName;
}
