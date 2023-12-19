package com.M2D.EmissionAssessment.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{
    String message;

    public ResourceNotFoundException(String message) {
        super(String.format("%s", message));

        this.message = message;
    }
}

