package com.M2D.EmissionAssessment.Model;

import com.M2D.EmissionAssessment.MultiTenancy.RequestInterceptor;
import jakarta.persistence.*;

import lombok.*;

import java.sql.Timestamp;
import java.time.ZonedDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "emission_assessment", schema = "sms_ea", catalog = "")
public class EAModel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    private String name;

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Basic
    @Column(name = "period_index", nullable = true)
    private Long periodIndex;

    @Basic
    @Column(name = "org_site_index", nullable = true)
    private Long orgSiteIndex;

    @Basic
    @Column(name = "status", nullable = true)
    private String status;

    @Basic
    @Column(name = "created_date", nullable = true)
    private ZonedDateTime createdDate;

    @Basic
    @Column(name = "created_by", nullable = true, length = 255)
    private String createdBy;

    @Basic
    @Column(name = "updated_date", nullable = true)
    private ZonedDateTime updatedDate;

    @Basic
    @Column(name = "updated_by", nullable = true, length = 255)
    private String updatedBy;

    @Basic
    @Column(name = "baseline_total_emission", nullable = true, length = 255)
    private String baselineTotalEmission;

    @Basic
    @Column(name = "assesser_index", nullable = true)
    private Long assesserIndex;

    @Basic
    @Column(name = "start_date", nullable = true)
    private ZonedDateTime startDate;

    @Basic
    @Column(name = "finish_date", nullable = true)
    private ZonedDateTime finishDate;

    @Basic
    @Column(name = "organization_index", nullable = true)
    private Long organizationIndex;


    @PrePersist
    public void created() {
        createdDate = ZonedDateTime.now();
        createdBy = String.valueOf(RequestInterceptor.UserId);
        organizationIndex=RequestInterceptor.OrgId;
    }

    @PreUpdate
    public void updated() {
        updatedDate = ZonedDateTime.now();
        updatedBy  = String.valueOf(RequestInterceptor.UserId);
        organizationIndex=RequestInterceptor.OrgId;
    }



}
