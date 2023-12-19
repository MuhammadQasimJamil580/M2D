package com.M2D.EmissionAssessment.Repository;


import com.M2D.EmissionAssessment.Model.EmissionAlterationHistoryModel;
import com.M2D.EmissionAssessment.Model.InventoryAlterationHistoryModel;
import com.M2D.EmissionAssessment.Model.InventoryModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryAlterationHistoryRepo extends JpaRepository<InventoryAlterationHistoryModel,Long> {

//    void deleteByInventoryIndex(InventoryModel inventoryIndex);

    InventoryAlterationHistoryModel findByInventoryIndexAndEmissionAlterationHistoryIndex(InventoryModel inventoryIndex, EmissionAlterationHistoryModel emissionAlterationHistoryIndex);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM inventory_alteration_history  WHERE inventory_index IN (:idValue)", nativeQuery = true)
    void deletewhereInvIndexIn (@Param("idValue") String[] invIndexes);
}
