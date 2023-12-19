package com.M2D.EmissionAssessment.ServiceImpl;

import com.M2D.EmissionAssessment.DTO.Response.EABaseLineGraphDto;
import com.M2D.EmissionAssessment.DTO.Response.EAGraphPonitsDto;
import com.M2D.EmissionAssessment.DTO.Response.InventoryStatusDto;
import com.M2D.EmissionAssessment.Exception.ResourceNotFoundException;
import com.M2D.EmissionAssessment.Exception.ServiceError;
import com.M2D.EmissionAssessment.Exception.ServiceException;
import com.M2D.EmissionAssessment.Helper.ResponseDto;
import com.M2D.EmissionAssessment.Model.EAModel;
import com.M2D.EmissionAssessment.Model.EmissionAlterationHistoryModel;
import com.M2D.EmissionAssessment.Model.InventoryAlterationHistoryModel;
import com.M2D.EmissionAssessment.Model.InventoryModel;
import com.M2D.EmissionAssessment.Repository.EARepo;
import com.M2D.EmissionAssessment.Repository.EmissionAlterationHistoryRepo;
import com.M2D.EmissionAssessment.Repository.InventoryAlterationHistoryRepo;
import com.M2D.EmissionAssessment.Repository.InventoryRepo;
import com.M2D.EmissionAssessment.Services.EmissionAssessmentGraphService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmissionAssessmentGraphServiceImpl implements EmissionAssessmentGraphService {


    final EARepo eaRepo;
    final EmissionAlterationHistoryRepo emissionAlterationHistoryRepo;
    final InventoryRepo inventoryRepo;
    final InventoryAlterationHistoryRepo inventoryAlterationHistoryRepo;

    public EmissionAssessmentGraphServiceImpl(EARepo eaRepo, EmissionAlterationHistoryRepo emissionAlterationHistoryRepo, InventoryRepo inventoryRepo, InventoryAlterationHistoryRepo inventoryAlterationHistoryRepo) {
        this.eaRepo = eaRepo;
        this.emissionAlterationHistoryRepo = emissionAlterationHistoryRepo;
        this.inventoryRepo = inventoryRepo;
        this.inventoryAlterationHistoryRepo = inventoryAlterationHistoryRepo;
    }

    @Override
    public EABaseLineGraphDto findAllEaById(Long id) {

        EAModel eaModel = eaRepo.findByIdAndStatusNot(id, "-1");
        if (eaModel != null) {
            EABaseLineGraphDto EABaseLineGraphDto = new EABaseLineGraphDto();
            List<EmissionAlterationHistoryModel> emissionAlterationHistoryModelList
                    = emissionAlterationHistoryRepo.findByEmissionIndex(eaModel);
            List<EAGraphPonitsDto> EAGraphPonitsDtolist = new ArrayList<>(emissionAlterationHistoryModelList.size());
            emissionAlterationHistoryModelList.forEach(emissionAlterationHistoryModel -> {
                EAGraphPonitsDto EAGraphPonitsDto = new EAGraphPonitsDto();
                EAGraphPonitsDto.setTotal_Emission(emissionAlterationHistoryModel.getTotalEmission());
                EAGraphPonitsDto.setDate(emissionAlterationHistoryModel.getCreatedDate());
                EAGraphPonitsDto.setId(emissionAlterationHistoryModel.getId());
                EAGraphPonitsDtolist.add(EAGraphPonitsDto);
            });

            EABaseLineGraphDto.setTotalBaseLine(eaModel.getBaselineTotalEmission());
            EABaseLineGraphDto.setEAGraphPonitsDtoList(EAGraphPonitsDtolist);
            return EABaseLineGraphDto;
        } else {
            ResponseDto<Object> responseDto = new ResponseDto<>();
            responseDto.setStatusCode(HttpStatus.BAD_REQUEST.toString());
            responseDto.setMessage("Not find Emission Assessment id");
        }
        return null;
    }

    @Override
    public List<InventoryStatusDto> findAllInventoryStatus(Long EAid, Long pointId) {
        String[] statuslist = {"1"};
        EAModel eaModel = eaRepo.findByIdAndStatusNot(EAid, "-1");
        Optional<EmissionAlterationHistoryModel> eamModel = emissionAlterationHistoryRepo.findById(pointId);

        List<InventoryStatusDto> inventoryStatusDtolist;
        if (eamModel.isPresent()) {
            List<InventoryModel> inventoryModellist = inventoryRepo.findByActivityIndexAndStatusIn(eaModel.getId(), statuslist).orElseThrow(() -> new ResourceNotFoundException("Not found"));
            inventoryStatusDtolist = new ArrayList<>(inventoryModellist.size());
            inventoryModellist.forEach(inventoryModel -> {
                InventoryStatusDto inventoryStatusDto = new InventoryStatusDto();
                inventoryStatusDto.setInventoryName(inventoryModel.getName());
                InventoryAlterationHistoryModel inventoryAlterationHistoryModel =
                        inventoryAlterationHistoryRepo.findByInventoryIndexAndEmissionAlterationHistoryIndex(inventoryModel, eamModel.get());
                if (inventoryAlterationHistoryModel != null) {

                    String TCO2 = inventoryAlterationHistoryModel.getTotalEmission();
                    if(TCO2.isEmpty()){
                        throw new ServiceException(ServiceError.EA001);
                    }

                    else {

                        inventoryStatusDto.setTCO2(TCO2);
                    }

                    String Baseline = inventoryModel.getBaselineTco2();
                    if(Baseline.isEmpty()){

                        throw new ServiceException(ServiceError.EA002);
                    }else {

                        inventoryStatusDto.setBaseLineValue(Baseline);
                    }
                    inventoryStatusDto.setChange(String.valueOf((Double.parseDouble(Baseline) - Double.parseDouble(TCO2))));
                    inventoryStatusDtolist.add(inventoryStatusDto);
                }
            });
        } else {
            inventoryStatusDtolist = null;
        }
        return inventoryStatusDtolist;
    }
}