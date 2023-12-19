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
@Table(name = "inventory_alteration_history", schema = "sms_ea", catalog = "")
public class InventoryAlterationHistoryModel {



    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "tco2", nullable = true, length = 255)
    private String totalEmission;


    @Basic
    @Column(name = "created_date", nullable = true)
    private ZonedDateTime createdDate;

    @Basic
    @Column(name = "created_by", nullable = true, length = 255)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "inventory_index", referencedColumnName = "id")
    private InventoryModel inventoryIndex;


    @ManyToOne
    @JoinColumn(name = "emission_alteration_history_Index", referencedColumnName = "id")
    private EmissionAlterationHistoryModel emissionAlterationHistoryIndex;

    @PrePersist
    public void created() {
        createdDate = ZonedDateTime.now();
        createdBy = String.valueOf(RequestInterceptor.UserId);
    }

}
