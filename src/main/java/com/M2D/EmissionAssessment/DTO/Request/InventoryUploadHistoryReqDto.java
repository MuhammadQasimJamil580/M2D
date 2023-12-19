package com.M2D.EmissionAssessment.DTO.Request;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUploadHistoryReqDto {

    private Long id;

    private Integer acceptedInventories;

    private Integer rejectedInventories;

    private String status;

    private EADtoUploadHistoryReq emission_index;

    private ZonedDateTime createdDate;

    private Long createdBy;

}
