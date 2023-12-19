package com.M2D.EmissionAssessment.Services;

import com.M2D.EmissionAssessment.DTO.Request.InventoryBulkUploadDtoReq;
import com.M2D.EmissionAssessment.DTO.Response.InventoryUploadDto_Resp;

import java.util.List;

public interface InventoryBulkUploadService {

List<InventoryUploadDto_Resp> inventoryBulkUpload(List<InventoryBulkUploadDtoReq> inventoryBulkUploadDtoReqList);




}
