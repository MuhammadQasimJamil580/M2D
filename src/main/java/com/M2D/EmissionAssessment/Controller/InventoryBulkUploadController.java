package com.M2D.EmissionAssessment.Controller;

import com.M2D.EmissionAssessment.DTO.Request.InventoryBulkUploadDtoReq;
import com.M2D.EmissionAssessment.DTO.Request.InventoryUploadDto_Req;
import com.M2D.EmissionAssessment.DTO.Response.InventoryUploadDto_Resp;
import com.M2D.EmissionAssessment.Services.InventoryBulkUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value ="/InventoryBulkUpload")
public class InventoryBulkUploadController {

    @Autowired
    InventoryBulkUploadService inventoryBulkUploadService;
    @PostMapping("/uploadExcell")
    public ResponseEntity<List<InventoryUploadDto_Resp>> processExcelFile(@RequestBody List <InventoryBulkUploadDtoReq> inventoryBulkUploadDtoReqList) {
        List<InventoryUploadDto_Resp> itemDataList = inventoryBulkUploadService.inventoryBulkUpload(inventoryBulkUploadDtoReqList);
        return ResponseEntity.ok(itemDataList);
    }


}
