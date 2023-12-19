package com.M2D.EmissionAssessment.Helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T>{
    private T data;
    private String message;
    private String statusCode;


}
