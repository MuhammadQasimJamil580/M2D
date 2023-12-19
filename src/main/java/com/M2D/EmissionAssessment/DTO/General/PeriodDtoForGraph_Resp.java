package com.M2D.EmissionAssessment.DTO.General;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PeriodDtoForGraph_Resp {
    private String periodName;
    private Long[] id;


}
