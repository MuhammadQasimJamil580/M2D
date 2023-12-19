package com.M2D.EmissionAssessment.Controller;

import com.M2D.EmissionAssessment.DTO.Response.TenantDTO_Resp;
import com.M2D.EmissionAssessment.MultiTenancy.DataSourceConfig;
import com.M2D.EmissionAssessment.Services.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/tenant")
@CrossOrigin
public class TenantController {

    @Autowired
    TenantService tenantService;

//    @GetMapping("/{oId}")
//    TenantDTO_Resp getDbNames(@PathVariable("oId") Long oId) {
//        TenantDTO_Resp tenantDTO_resp = this.tenantService.getDbNames(oId);
//        return tenantDTO_resp;
//    }


    @GetMapping("/getAllTanetData")
    List<DataSourceConfig> getTenantDataForDbConfig() {
      List <DataSourceConfig> tenantDTO_resp = this.tenantService.getAllDbNames();
        return tenantDTO_resp;
    }
}
