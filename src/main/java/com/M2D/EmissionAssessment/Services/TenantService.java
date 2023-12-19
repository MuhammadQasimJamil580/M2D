package com.M2D.EmissionAssessment.Services;

import com.M2D.EmissionAssessment.MultiTenancy.DataSourceConfig;

import java.util.List;

public interface TenantService {

    List<DataSourceConfig> getAllDbNames();



}
