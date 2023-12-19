package com.M2D.EmissionAssessment.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalEmissionsGraphDto {
    List<String> periodNames;
    List<Double> scope1Emissions;
    List<Double> scope2Emissions;
    List<Double> scope3Emissions;

}
