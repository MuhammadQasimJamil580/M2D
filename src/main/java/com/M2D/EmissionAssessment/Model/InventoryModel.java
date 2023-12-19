package com.M2D.EmissionAssessment.Model;

import com.M2D.EmissionAssessment.MultiTenancy.RequestInterceptor;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.ZonedDateTime;


@Entity
@Getter
@Setter
@Table(name = "assesment_inventory", schema = "sms_ea", catalog = "")

public class InventoryModel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = true, length = 1000)
    private String name;

    @Basic
    @Column(name = "category_index", nullable = true)
    private Long categoryIndex;

    @Basic
    @Column(name = "subcategory_index", nullable = true)
    private Long subcategoryIndex;

    @Basic
    @Column(name = "factor", nullable = true, length = 255)
    private String factor;

    @Basic
    @Column(name = "factor_detail", nullable = true, length = 255)
    private String factorDetail;

    @Basic
    @Column(name = "description", nullable = true, length = 1000)
    private String description;

    @Basic
    @Column(name = "tag_index", nullable = true)
    private Long tagIndex;

    @Basic
    @Column(name = "gas_index", nullable = true)
    private Long gasIndex;

    @Basic
    @Column(name = "consumption", nullable = true, length = 255)
    private String consumption;

    @Basic
    @Column(name = "consumption_unit", nullable = true, length = 255)
    private String consumptionUnit;

    @Basic
    @Column(name = "tco2", nullable = true, length = 255)
    private String tco2;

    @Basic
    @Column(name = "baseline_tco2", nullable = true, length = 255)
    private String baselineTco2;

    @Basic
    @Column(name = "status", nullable = true, length = 255)
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
    @Column(name = "updated_by", nullable = true, length = 5)
    private String updatedBy;

    @Basic
    @Column(name = "standard_index", nullable = true)
    private Long standardIndex;

    @Basic
    @Column(name = "usages", nullable = true, length = 255)
    private String usages;

    @Basic
    @Column(name = "usage_unit", nullable = true, length = 255)
    private String usageUnit;

    @ManyToOne
    @JoinColumn(name = "activity_index", referencedColumnName = "id")
    private EAModel activityIndex;

    @ManyToOne
    @JoinColumn(name = "inventory_upload_history_index", referencedColumnName = "id")
    private InventoryUploadHistoryModel inventoryUploadHistoryModelIndex;


    @PrePersist
    public void created() {
        createdDate = ZonedDateTime.now();
        createdBy = String.valueOf(RequestInterceptor.UserId);
    }

    @PreUpdate
    public void updated() {
        updatedDate = ZonedDateTime.now();
        updatedBy  = String.valueOf(RequestInterceptor.UserId);
    }

}
