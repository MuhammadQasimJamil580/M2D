package com.M2D.EmissionAssessment.Services;

import com.M2D.EmissionAssessment.DTO.Response.HistoricalEmissionsGraphDto;
import com.M2D.EmissionAssessment.DTO.Response.ScopeAndTagwiseEmission;
import com.M2D.EmissionAssessment.DTO.Response.UseTagToGetEmissionGraphDto;

import java.util.List;

public interface MainDashboardGraphsService {

   public ScopeAndTagwiseEmission getFilteredDataForScopeWiseGraph(Long[] siteIndexes, Long[] periodIndexes);
   public HistoricalEmissionsGraphDto getFilteredDataForHistoricEmissions(Long[] siteIndexes, Long[] periodIndexes);


   List<UseTagToGetEmissionGraphDto> findAllTCO2ThroughTag();

   List<UseTagToGetEmissionGraphDto> findAllTCo2ThroughOrgSitAndPeriod(Long[]  orgSiteIndex, Long[]  PeriodIndex);


}
