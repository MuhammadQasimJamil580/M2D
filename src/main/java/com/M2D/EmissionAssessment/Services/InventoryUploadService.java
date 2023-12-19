package com.M2D.EmissionAssessment.Services;

import com.M2D.EmissionAssessment.DTO.Request.InventoryDTO_Req;
import com.M2D.EmissionAssessment.DTO.Request.InventoryUploadDto_Req;
import com.M2D.EmissionAssessment.DTO.Request.InventoryUploadHistoryReqDto;
import com.M2D.EmissionAssessment.DTO.Response.InventoryDTO_Resp;
import com.M2D.EmissionAssessment.DTO.Response.InventoryUploadDto_Resp;
import com.M2D.EmissionAssessment.DTO.Response.InventoryUploadHistoryRespDto;


import java.util.List;

public interface InventoryUploadService {

    public List<InventoryUploadDto_Resp> mapFactorInformationt(List<InventoryUploadDto_Req> inventoryUploadDto_reqList);

    public void saveAllInventories(List<InventoryDTO_Req> inventoryDTO_req);


}
