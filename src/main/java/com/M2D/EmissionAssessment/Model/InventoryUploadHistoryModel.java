package com.M2D.EmissionAssessment.Model;

import com.M2D.EmissionAssessment.MultiTenancy.RequestInterceptor;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "inventory_upload_history", schema = "sms_ea", catalog = "")
public class InventoryUploadHistoryModel {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "accepted_inventories", nullable = true, length = 255)
    private Integer acceptedInventories;

    @Basic
    @Column(name = "rejected_inventories", nullable = true)
    private Integer rejectedInventories;


    @Basic
    @Column(name = "status", nullable = true, length = 255)
    private String status;

    @Basic
    @Column(name = "created_date", nullable = true)
    private ZonedDateTime createdDate;

    @Basic
    @Column(name = "created_by", nullable = true, length = 255)
    private Long createdBy;


    @ManyToOne
    @JoinColumn(name = "emission_index", referencedColumnName = "id")
    private EAModel emissionIndex;

    @PrePersist
    public void created() {
        createdDate = ZonedDateTime.now();
        createdBy = RequestInterceptor.UserId;
    }

}
