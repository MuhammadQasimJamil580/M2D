package com.M2D.EmissionAssessment.Repository;


import com.M2D.EmissionAssessment.Model.EAModel;
import com.M2D.EmissionAssessment.Model.EmissionAlterationHistoryModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmissionAlterationHistoryRepo extends JpaRepository<EmissionAlterationHistoryModel,Long> {
   List<EmissionAlterationHistoryModel> findByEmissionIndex(EAModel emissionIndex);

   @Transactional
   @Modifying
   @Query(value = "DELETE FROM emission_alteration_history  WHERE emission_index IN (:emissionId)", nativeQuery = true)
   void deleteByEmissionIndex (@Param("emissionId") Long eaModel);



}
