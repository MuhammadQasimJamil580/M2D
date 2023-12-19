package com.M2D.EmissionAssessment.DTO.Response;

import com.M2D.EmissionAssessment.DTO.Request.EADTO_Req;
import lombok.Data;

@Data
public class InventoryDTO_Resp {
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
    private String categoryname;
    private String Scope;
    private String subCategoryname;
    private String standardName;
    private String tagName;
    private String gassName;
    private EADTO_Req activityIndex;
}
