package com.M2D.EmissionAssessment.DTO.General;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScopeWiseCategoryIndexListDto {
    String[] scope1CategoryIndexes;
    String[] scope2CategoryIndexes;
    String[] scope3CategoryIndexes;
}
