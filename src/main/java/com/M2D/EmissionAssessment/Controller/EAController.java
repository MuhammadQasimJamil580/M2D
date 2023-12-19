package com.M2D.EmissionAssessment.Controller;

import com.M2D.EmissionAssessment.DTO.Request.EADTO_Req;
import com.M2D.EmissionAssessment.DTO.Response.EADTO_Resp;
import com.M2D.EmissionAssessment.Exception.ApiResponse;
import com.M2D.EmissionAssessment.Services.EAService;
import com.M2D.EmissionAssessment.Services.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/EmissionAssessment")
@AllArgsConstructor
public class EAController {


    @Autowired
     EAService eaService;

    @Autowired
    InventoryService inventoryService;

    @GetMapping("/All")
    public  ResponseEntity<List<EADTO_Resp>> getAll(){
        List<EADTO_Resp> eadto_resps = eaService.getAll();
        return  new ResponseEntity<>(eadto_resps, HttpStatus.OK);
    }

    @GetMapping("/AllForDropdowns")
    public  List<EADTO_Req> getEADropdown(){
        List<EADTO_Req> eadto_reqs = eaService.getAllEADropdown();
        return  eadto_reqs;
    }

    @GetMapping("/AllBaselineEA")
    public  List<EADTO_Req> getEABaselineDropdown(){
        List<EADTO_Req> eadto_reqs = eaService.getAllBaselineEADropdown();
        return  eadto_reqs;
    }

    @GetMapping("/ById/{id}")
    public ResponseEntity<EADTO_Req> getById(@PathVariable Long id){
        EADTO_Req eadto_req = eaService.getOneRecord(id);
    return new ResponseEntity<>(eadto_req,HttpStatus.OK);
    }

    @PostMapping("/save")
    public  ResponseEntity<ApiResponse> saveEA(@RequestBody EADTO_Req eadto_req){
    eaService.saveEA(eadto_req);
    return new ResponseEntity<>(new ApiResponse("Saved successfully",true), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public  ResponseEntity<ApiResponse> updateEA(@RequestBody EADTO_Req eadto_req, @PathVariable Long id){
        eaService.updateEA(eadto_req,id);
        return new ResponseEntity<>(new ApiResponse("Updated successfully",true),HttpStatus.OK);
    }

    @PutMapping("/deactivate/{id}")
    public  ResponseEntity deleteEA(@PathVariable Long id){
        eaService.deactivateEA(id);
        return  new ResponseEntity<>(new ApiResponse("deactivated",true),HttpStatus.OK);
    }

    @PutMapping("/baseline/{id}")
    public  ResponseEntity baselineEA(@PathVariable Long id){
        eaService.baseLineThisEA(id);
        inventoryService.baseLineThisInventory(id);
        return  new ResponseEntity<>(new ApiResponse("baselined successfully",true),HttpStatus.OK);
    }


    @PutMapping("/removeBaseline/{id}")
    public  ResponseEntity removeBaseline(@PathVariable Long id){
        inventoryService.removeBaselineForThisInventory(id);
        eaService.removeBaselineForThisEA(id);
        return  new ResponseEntity<>(new ApiResponse("baseline removed successfully",true),HttpStatus.OK);
    }



    @PutMapping("/activate/{id}")
    public  ResponseEntity activateEA(@PathVariable Long id){

        eaService.activateEA(id);
        return  new ResponseEntity<>(new ApiResponse("activated",true),HttpStatus.OK);

    }






}
