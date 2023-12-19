package com.M2D.EmissionAssessment.Controller;


import com.M2D.EmissionAssessment.DTO.General.InventoryDataForRIDTO;
import com.M2D.EmissionAssessment.DTO.Request.InventoryDTO_Req;
import com.M2D.EmissionAssessment.DTO.Response.InventoryDTO_Resp;
import com.M2D.EmissionAssessment.Exception.ApiResponse;
import com.M2D.EmissionAssessment.Model.InventoryModel;
import com.M2D.EmissionAssessment.Services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/Inventory")
public class InventoryController {
    @Autowired
    InventoryService inventoryService;

    public  InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/All/{eId}")
    public  ResponseEntity<List<InventoryDTO_Resp>> getAll(@PathVariable Long eId){
        List<InventoryDTO_Resp> inventoryDTO_resps = inventoryService.getAll(eId);
        return  new ResponseEntity<>(inventoryDTO_resps, HttpStatus.OK);
    }

    @GetMapping("/ById/{iId}")
    public  ResponseEntity<InventoryDTO_Req> getOneRecord(@PathVariable Long iId){
        InventoryDTO_Req inventoryDTO_req = inventoryService.getOneRecord(iId);
        return  new ResponseEntity<>(inventoryDTO_req, HttpStatus.OK);
    }

    @PostMapping("/save")
    public  ResponseEntity<ApiResponse> saveInventory(@RequestBody InventoryDTO_Req inventoryDTO_req){
        inventoryService.saveInventory(inventoryDTO_req);
        return new ResponseEntity<>(new ApiResponse("created successfully",true), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public  ResponseEntity<ApiResponse> updateInventory(@RequestBody InventoryDTO_Req inventoryDTO_req, @PathVariable Long id){
        inventoryService.updateInventory(inventoryDTO_req,id);
        return new ResponseEntity<>(new ApiResponse("updated successfully",true),HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public  ResponseEntity deleteInventory(@PathVariable Long id){
        inventoryService.deleteInventory(id);
        return  new ResponseEntity<>(new ApiResponse("deleted successfully",true),HttpStatus.OK);
    }



    @GetMapping("/allInventoryDataHMPForRI")
    public HashMap<Long,String> allInventoryDataHMPForRI(){
        HashMap<Long,String> inventoryMap = inventoryService.getHMPOfInventorForRI();
        return inventoryMap;
    }
    @GetMapping("/allInventoryDataForRI")
    public HashMap<Long , List<InventoryDataForRIDTO> > allInventoryDataForRI(){
        HashMap<Long , List<InventoryDataForRIDTO> > inventorydata = inventoryService.getListOFInventorForRI();
        return inventorydata;
    }


}
