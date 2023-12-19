package com.M2D.EmissionAssessment.Controller;



import com.M2D.EmissionAssessment.DTO.Response.InventoryDTO_Resp;
import com.M2D.EmissionAssessment.DTO.Response.InventoryUploadHistoryRespDto;
import com.M2D.EmissionAssessment.Services.InventoryUploadHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/inventoryUploadHistory")
public class InventoryUploadHistoryController {
    @Autowired
    InventoryUploadHistoryService inventoryUploadHistoryService;

    @GetMapping("/getAllScanned/{id}")
    public ResponseEntity<List<InventoryDTO_Resp>> getAllScanned(@PathVariable Long id) {
        List<InventoryDTO_Resp> response = inventoryUploadHistoryService.getAllScanned(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<InventoryUploadHistoryRespDto>> getAll() {
        List<InventoryUploadHistoryRespDto> response = inventoryUploadHistoryService.getAll();
        return ResponseEntity.ok(response);
    }




}
