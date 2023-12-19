package com.M2D.EmissionAssessment.Repository;


import com.M2D.EmissionAssessment.Model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TenantRepo extends JpaRepository<Tenant, Long> {


}
