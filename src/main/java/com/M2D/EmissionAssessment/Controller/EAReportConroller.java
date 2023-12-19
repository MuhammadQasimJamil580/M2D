package com.M2D.EmissionAssessment.Controller;

import com.M2D.EmissionAssessment.Services.EAReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/EAReport")
public class EAReportConroller {

    @Autowired
    EAReportService eaReportService;

@GetMapping("/{eaId}")
    public ResponseEntity<byte[]> getPdfReport(@PathVariable Long eaId) throws IOException {
    return eaReportService.getPDFReport(eaId);

        
    }

}
