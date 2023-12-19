package com.M2D.EmissionAssessment.ServiceImpl;

import com.M2D.EmissionAssessment.Model.Tenant;
import com.M2D.EmissionAssessment.MultiTenancy.DataSourceConfig;
import com.M2D.EmissionAssessment.Repository.TenantRepo;
import com.M2D.EmissionAssessment.Services.TenantService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantRepo tenantRepo;

    @Autowired
    private ModelMapper modelMapper;





    /**
     * @param
     * @return
     */
    @Override
    public List<DataSourceConfig> getAllDbNames() {
        List <Tenant> tenantList = tenantRepo.findAll();
        List <DataSourceConfig> tenantDTO_respList = new ArrayList<>();
        for(Tenant tenant : tenantList){
            DataSourceConfig tenantDTO_resp = modelMapper.map(tenant,DataSourceConfig.class);
            tenantDTO_respList.add(tenantDTO_resp);
        }
        return tenantDTO_respList;
    }


}
