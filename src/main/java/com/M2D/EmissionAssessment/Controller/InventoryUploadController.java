package com.M2D.EmissionAssessment.Controller;

import com.M2D.EmissionAssessment.DTO.Request.InventoryDTO_Req;
import com.M2D.EmissionAssessment.DTO.Request.InventoryUploadDto_Req;
import com.M2D.EmissionAssessment.DTO.Request.InventoryUploadHistoryReqDto;
import com.M2D.EmissionAssessment.DTO.Response.InventoryUploadDto_Resp;
import com.M2D.EmissionAssessment.DTO.Response.InventoryUploadHistoryRespDto;
import com.M2D.EmissionAssessment.Exception.ApiResponse;
import com.M2D.EmissionAssessment.Services.InventoryUploadHistoryService;
import com.M2D.EmissionAssessment.Services.InventoryUploadService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/InventoryUploadController")
public class InventoryUploadController {

    @Autowired
    InventoryUploadService inventoryUploadService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebClient.Builder webclient;

    @PostMapping("/uploadExcell")
    public ResponseEntity<List<InventoryUploadDto_Resp>> processExcelFile(@RequestBody List <InventoryUploadDto_Req> inventoryUploadDto_reqs) {
        List<InventoryUploadDto_Resp> itemDataList = inventoryUploadService.mapFactorInformationt(inventoryUploadDto_reqs);
        return ResponseEntity.ok(itemDataList);
    }


    @PostMapping("/saveAll")
    public  ResponseEntity<ApiResponse> saveBulkInventory(@RequestBody List<InventoryDTO_Req> inventoryDTO_req){
        inventoryUploadService.saveAllInventories(inventoryDTO_req);
        return new ResponseEntity<>(new ApiResponse("created successfully",true), HttpStatus.CREATED);
    }



}
