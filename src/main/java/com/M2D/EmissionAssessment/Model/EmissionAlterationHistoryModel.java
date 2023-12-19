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
@Table(name = "emission_alteration_history", schema = "sms_ea", catalog = "")
public class EmissionAlterationHistoryModel {




        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        @Column(name = "id", nullable = false)
        private Long id;



        @Basic
        @Column(name = "total_emission", nullable = true, length = 255)
        private String totalEmission;

        @Basic
        @Column(name = "created_date", nullable = true)
        private ZonedDateTime createdDate;


        @Basic
        @Column(name = "created_by", nullable = true, length = 255)
        private String createdBy;


        @ManyToOne
        @JoinColumn(name = "emission_index", referencedColumnName = "id")
        private EAModel emissionIndex;


        @PrePersist
        public void created() {
                createdDate = ZonedDateTime.now();
                createdBy = String.valueOf(RequestInterceptor.UserId);
        }






}
