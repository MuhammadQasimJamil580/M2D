package com.M2D.EmissionAssessment.Controller;

import com.M2D.EmissionAssessment.DTO.Response.HistoricalEmissionsGraphDto;
import com.M2D.EmissionAssessment.DTO.Response.ScopeAndTagwiseEmission;
import com.M2D.EmissionAssessment.DTO.Response.UseTagToGetEmissionGraphDto;
import com.M2D.EmissionAssessment.Services.MainDashboardGraphsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/MainDashboardGraphs")
public class MainDashboardGraphs {

    @Autowired
    MainDashboardGraphsService mainDashboardGraphsService;


    @GetMapping("/getDefaultDataForScopeWiseGraph/{siteIndexes}/{periodIndexes}")
    public ScopeAndTagwiseEmission getFilteredDataForScopeWiseGraph(@PathVariable Long[] siteIndexes,@PathVariable Long[] periodIndexes){
        return mainDashboardGraphsService.getFilteredDataForScopeWiseGraph(siteIndexes,periodIndexes);
    }

    @GetMapping("/getDefaultHistoricEmissions/{siteIndexes}/{periodIndexes}")
    public HistoricalEmissionsGraphDto getHistoricEmissions(@PathVariable Long[] siteIndexes, @PathVariable Long[] periodIndexes){
        return mainDashboardGraphsService.getFilteredDataForHistoricEmissions(siteIndexes,periodIndexes);
    }
    @GetMapping("/getDefaultDonutChartData/{siteIndexes}/{periodIndexes}")
    public List<UseTagToGetEmissionGraphDto> findAlltag() {
        return mainDashboardGraphsService.findAllTCO2ThroughTag();
    }

    @GetMapping("/getFilteredDonutChartData/{OrgSieIndex}/{PeriodIndex}")
    public List<UseTagToGetEmissionGraphDto> findAllTCO2(@PathVariable Long[] OrgSieIndex, @PathVariable Long[]  PeriodIndex){
        return mainDashboardGraphsService.findAllTCo2ThroughOrgSitAndPeriod(OrgSieIndex, PeriodIndex);

    }


}
