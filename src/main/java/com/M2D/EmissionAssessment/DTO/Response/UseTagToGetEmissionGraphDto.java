package com.M2D.EmissionAssessment.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UseTagToGetEmissionGraphDto {

    private String TagName;
    private Double TCO2;

}
