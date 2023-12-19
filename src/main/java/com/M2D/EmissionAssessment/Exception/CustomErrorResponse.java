package com.M2D.EmissionAssessment.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@RequiredArgsConstructor
public class CustomErrorResponse {
    private String statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;
}