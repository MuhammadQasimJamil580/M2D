package com.M2D.EmissionAssessment.DTO.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUploadDto_Req {

    private String inventoryName;
    private Double consumption;
    private String consumptionUnit;
    private Long activity_index;

}
