package com.M2D.EmissionAssessment.DTO.Response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EABaseLineGraphDto {
    private String totalBaseLine;
    private List<EAGraphPonitsDto> EAGraphPonitsDtoList;
}

