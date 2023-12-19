package com.M2D.EmissionAssessment.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tenant", schema = "smsglobal")
public class Tenant {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "ea_db_url", nullable = true, length = 255)
    private String eaDbName;
    @Basic
    @Column(name = "ri_db_url", nullable = true, length = 255)
    private String riDbName;
    @Basic
    @Column(name = "admin_db_url", nullable = true, length = 255)
    private String adminDbName;

    @Basic
    @Column(name = "status", nullable = true, length = 255)
    private String status;

    @Basic
    @Column(name = "user_name", nullable = true, length = 255)
    private String userName;

    @Basic
    @Column(name = "password", nullable = true, length = 255)
    private String password;

    @Basic
    @Column(name = "driver_class_name", nullable = true, length = 255)
    private String driver_class_name;

    @Basic
    @Column(name = "organization_index")
    private  Long  organizationIndex;

}
