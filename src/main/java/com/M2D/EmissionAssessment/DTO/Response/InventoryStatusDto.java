package com.M2D.EmissionAssessment.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InventoryStatusDto {

    private String inventoryName;
    private String baseLineValue;
    private String TCO2;
    private String Change;
}
