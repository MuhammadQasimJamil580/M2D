package com.M2D.EmissionAssessment.DTO.General;

import lombok.Data;

@Data
public class InventoryDataForRIDTO {

    private Long id;
    private String name;
    private String tco2;
    private String periodName;
    private String scope;
}
