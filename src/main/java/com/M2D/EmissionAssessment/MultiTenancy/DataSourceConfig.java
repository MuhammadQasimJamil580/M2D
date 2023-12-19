package com.M2D.EmissionAssessment.MultiTenancy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DataSourceConfig {
    private Long id;
    private String eaDbName;
    private String riDbName;
    private String adminDbName;
    private String userName;
    private String password;
    private String driver_class_name;

}