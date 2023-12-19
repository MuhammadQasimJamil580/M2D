package com.M2D.EmissionAssessment.DTO.Request;

import lombok.Data;

@Data
public class InventoryDTO_Req {

    private Long id;
    private String name;
    private String factor;
    private String factorDetail;
    private String description;
    private String consumption;
    private String consumptionUnit;
    private String tco2;
    private String status;
    private String usages;
    private String usageUnit;
    private String scope;
    private Long activity_index;
    private Long standardIndex;
    private Long tagIndex;
    private Long gasIndex;
    private Long categoryIndex;
    private Long subcategoryIndex;


}
