package com.M2D.EmissionAssessment.MultiTenancy;

import com.M2D.EmissionAssessment.Exception.ResourceNotFoundException;
import com.M2D.EmissionAssessment.Services.TenantService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TenantDataSource implements Serializable {

    private HashMap<Long, DataSource> dataSources = new HashMap<>();
    private HashMap<Long,DataSourceConfig> dataSourceList = new HashMap<>();

    @Autowired
    private WebClient.Builder webclient;

    @Autowired
    TenantService tenantService;

    @Value("${tenantURL}")
    private String tenantURL;

//    @Autowired
//    private DataSourceConfigRepository configRepo;

    private DataSource createDataSource(Long id) throws Exception {
//      DataSourceConfig config = configRepo.findByFacilityId(id).orElseThrow();

        DataSourceConfig config = dataSourceList.get(id);
        DataSourceBuilder<?> factory = DataSourceBuilder
                .create().driverClassName(config.getDriver_class_name())
                .username(config.getUserName())
                .password(config.getPassword())
                .url(config.getEaDbName());
        return factory.build();
    }
    public DataSource getDataSource(Long id) throws Exception {
        if (dataSources.get(id) != null) {
            return dataSources.get(id);
        }
        DataSource dataSource = createDataSource(id);
        if (dataSource != null) {
            dataSources.put(id, dataSource);
        }
        return dataSource;
    }

    @PostConstruct
    public Map<String, DataSource> getAll() throws Exception {
        List<DataSourceConfig> configList = getApiResponse(tenantURL);
        Map<String, DataSource> result = new HashMap<>();
        for (DataSourceConfig config : configList) {
            DataSource dataSource = getDataSource(config.getId());
            result.put(config.getId().toString(), dataSource);
        }
        return result;
    }


    public List<DataSourceConfig> getApiResponse(String URL) {
        try {
            List<DataSourceConfig> container= new ArrayList<>();
            container = tenantService.getAllDbNames();
//                container = webclient
//                    .build()
//                    .get()
//                    .uri(URL)
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer (No Token required here)",HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                    .retrieve()
//                    .bodyToMono(new ParameterizedTypeReference<List<DataSourceConfig>>() {
//                    }).block();
                    for(DataSourceConfig dataSourceConfig : container){
                        dataSourceList.put(dataSourceConfig.getId(),dataSourceConfig);
                    }
            if(container == null){
                throw new ResourceNotFoundException("Service is down or there is no data in that service");
            }else{
                return container;
            }
        } catch (Exception e) {
            return null;
        }
    }

    }

