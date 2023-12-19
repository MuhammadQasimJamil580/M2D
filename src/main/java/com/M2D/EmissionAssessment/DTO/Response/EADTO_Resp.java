package com.M2D.EmissionAssessment.DTO.Response;

import lombok.Data;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Data
public class EADTO_Resp {

    private Long id;
    private String name;
    private String description;
    private Integer status;
    private ZonedDateTime startDate;
    private ZonedDateTime finishDate;
    private String siteName;
    private String periodName;
    private String assesserName;
    private Integer inventoryCount;
    private Integer totalEmission;
    private String baselineTotalEmission;


}
