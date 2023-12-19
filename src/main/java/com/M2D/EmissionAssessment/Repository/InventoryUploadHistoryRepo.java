package com.M2D.EmissionAssessment.Repository;


import com.M2D.EmissionAssessment.Model.EAModel;
import com.M2D.EmissionAssessment.Model.InventoryUploadHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InventoryUploadHistoryRepo extends JpaRepository<InventoryUploadHistoryModel, Long> {
    public List<InventoryUploadHistoryModel> findByStatusNot(String status);

    public List<InventoryUploadHistoryModel> findInventoryUploadHistoryModelByEmissionIndexAndStatusNot(EAModel emissionId, String status);

    public InventoryUploadHistoryModel findByIdAndStatusNot(Long id, String status);

    public InventoryUploadHistoryModel findInventoryUploadHistoryModelByEmissionIndex(EAModel eaModel);
}
