package com.M2D.EmissionAssessment.Services;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface EAReportService {

     ResponseEntity<byte[]> getPDFReport(Long eaId) throws IOException;


}
