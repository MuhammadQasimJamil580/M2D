package com.M2D.EmissionAssessment.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryBulkUploadDtoReq {

    private String inventoryName;
    private String invDetail;
    private String material;
    private String tagName;
    private Long consumption;
    private String usage;
    private String usageUnit;





}
