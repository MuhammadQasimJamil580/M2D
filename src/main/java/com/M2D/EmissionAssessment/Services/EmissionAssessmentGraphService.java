package com.M2D.EmissionAssessment.Services;

import com.M2D.EmissionAssessment.DTO.Response.EABaseLineGraphDto;
import com.M2D.EmissionAssessment.DTO.Response.InventoryStatusDto;
import com.M2D.EmissionAssessment.DTO.Response.InventoryStatusDtoList;

import java.util.List;

public interface EmissionAssessmentGraphService {

    EABaseLineGraphDto findAllEaById(Long id);

    List<InventoryStatusDto> findAllInventoryStatus(Long id, Long pointId);



}
