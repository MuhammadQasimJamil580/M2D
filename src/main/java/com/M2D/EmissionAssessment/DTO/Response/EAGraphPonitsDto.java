package com.M2D.EmissionAssessment.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EAGraphPonitsDto {

    private String Total_Emission;
    private ZonedDateTime date;
    private Long id;

}
