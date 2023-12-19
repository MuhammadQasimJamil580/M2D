package com.M2D.EmissionAssessment.Services;

import com.M2D.EmissionAssessment.DTO.Request.EADtoUploadHistoryReq;
import com.M2D.EmissionAssessment.DTO.Request.InventoryUploadHistoryReqDto;
import com.M2D.EmissionAssessment.DTO.Response.InventoryDTO_Resp;
import com.M2D.EmissionAssessment.DTO.Response.InventoryUploadHistoryRespDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InventoryUploadHistoryService {

    public List<InventoryDTO_Resp> getAllScanned(Long eaId);

    List<InventoryUploadHistoryRespDto> getAll();

    List<InventoryUploadHistoryRespDto> getByEmissionIndex(Long emissionIndex);

    String saveInventoryHistory(InventoryUploadHistoryReqDto inventoryUploadHistoryReqDto);

    String updateInventoryHistory(InventoryUploadHistoryReqDto inventoryUploadHistoryReqDto);

    String deleteInventoryHistory(Long id);
}
