package com.M2D.EmissionAssessment.DTO.Response;

import lombok.Data;

@Data
public class TenantDTO_Resp {

    private Long id;
    private String eaDbName;
    private String riDbName;
    private String adminDbName;
    private String userName;
    private String password;
    private String driver_class_name;

}
