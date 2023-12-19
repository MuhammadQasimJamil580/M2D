package com.M2D.EmissionAssessment.Services;

import com.M2D.EmissionAssessment.DTO.General.InventoryDataForRIDTO;
import com.M2D.EmissionAssessment.DTO.Request.InventoryDTO_Req;
import com.M2D.EmissionAssessment.DTO.Response.InventoryDTO_Resp;
import com.M2D.EmissionAssessment.Model.EmissionAlterationHistoryModel;
import com.M2D.EmissionAssessment.Model.InventoryModel;
import org.springframework.data.domain.Page;


import java.util.HashMap;
import java.util.List;

public interface InventoryService {


    List<InventoryDTO_Resp> getAll(Long eId );
    InventoryDTO_Req getOneRecord(Long invId );
    void saveInventory( InventoryDTO_Req inventoryDTO_req );
    void updateInventory(InventoryDTO_Req inventoryDTO_req,Long invId );
    void deleteInventory( Long invId );
    HashMap<Long,String> getHMPOfInventorForRI();
    HashMap<Long , List<InventoryDataForRIDTO> > getListOFInventorForRI();
    void baseLineThisInventory(Long eaId);
    void removeBaselineForThisInventory(Long eaId);

    void mentainInventoryAlterationHistory(InventoryModel inventoryModel, String toalEmission, String createdBy, EmissionAlterationHistoryModel emissionAlterationHistoryModel);



}
