package com.M2D.EmissionAssessment.DTO.Response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUploadDto_Resp {


//    private Long id;
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
    private String gassName;
    private String category;
    private String standard;
    private String subcategory;
    private Long activity_index;
    private Long standardIndex;
    private Long tagIndex;
    private Long gasIndex;
    private Long categoryIndex;
    private Long subcategoryIndex;

}
