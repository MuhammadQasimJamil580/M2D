package com.M2D.EmissionAssessment.Controller;

import com.M2D.EmissionAssessment.DTO.Response.EABaseLineGraphDto;
import com.M2D.EmissionAssessment.DTO.Response.InventoryStatusDto;
import com.M2D.EmissionAssessment.DTO.Response.InventoryStatusDtoList;
import com.M2D.EmissionAssessment.Services.EmissionAssessmentGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("EABaseline")
public class EAGraphController {

    @Autowired
    private EmissionAssessmentGraphService emissionAssessmentGraphService;

    @GetMapping("/{id}")
    public EABaseLineGraphDto getEmissionAssessmentGraph(@PathVariable Long id) {
        return emissionAssessmentGraphService.findAllEaById(id);
    }

    @GetMapping("/{emissionId}/{pointId}")
    public List<InventoryStatusDto> findAllInventoryStatus(@PathVariable Long emissionId, @PathVariable Long pointId) {
        return emissionAssessmentGraphService.findAllInventoryStatus(emissionId, pointId);
    }

}