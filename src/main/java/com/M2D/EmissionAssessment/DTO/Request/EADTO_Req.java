package com.M2D.EmissionAssessment.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EADTO_Req {

    private Long id;
    private String name;
    private String description;
    private Long periodIndex;
    private Long orgSiteIndex;
    private Integer status;
    private Long assesserIndex;
    private ZonedDateTime startDate;
    private ZonedDateTime finishDate;
    private Long organizationIndex;

}
