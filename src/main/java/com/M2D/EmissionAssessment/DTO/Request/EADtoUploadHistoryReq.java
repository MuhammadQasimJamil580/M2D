package com.M2D.EmissionAssessment.DTO.Request;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EADtoUploadHistoryReq {
    private Long id;
    private String name;
}
