package com.M2D.EmissionAssessment.ServiceImpl;

import com.M2D.EmissionAssessment.DTO.Request.InventoryDTO_Req;
import com.M2D.EmissionAssessment.DTO.Request.InventoryUploadDto_Req;
import com.M2D.EmissionAssessment.DTO.Response.InventoryUploadDto_Resp;
import com.M2D.EmissionAssessment.Exception.ResourceNotFoundException;
import com.M2D.EmissionAssessment.Model.EAModel;
import com.M2D.EmissionAssessment.Model.EmissionAlterationHistoryModel;
import com.M2D.EmissionAssessment.Model.InventoryModel;
import com.M2D.EmissionAssessment.Model.InventoryUploadHistoryModel;
import com.M2D.EmissionAssessment.MultiTenancy.RequestInterceptor;
import com.M2D.EmissionAssessment.Repository.EARepo;
import com.M2D.EmissionAssessment.Repository.InventoryRepo;
import com.M2D.EmissionAssessment.Repository.InventoryUploadHistoryRepo;
import com.M2D.EmissionAssessment.Services.EAService;
import com.M2D.EmissionAssessment.Services.InventoryService;
import com.M2D.EmissionAssessment.Services.InventoryUploadService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class InventoryUploadServiceImple implements InventoryUploadService {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private EAService eaService;

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InventoryUploadHistoryRepo inventoryUploadHistoryRepo;
    @Autowired
    private EARepo eaRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebClient.Builder webclient;

    @Value("${RefferenceDataURL}")
    private String RefferenceDataURL;

    @Override
    public List<InventoryUploadDto_Resp> mapFactorInformationt(List<InventoryUploadDto_Req> inventoryUploadDto_reqList) {
        Double consumption = 0.00;
        Double factor = 0.00;
        HashMap<String,String> refrenceData = new HashMap<String,String>();
        refrenceData = getApiResponse();
        String itemDescription = "";

        String quantity = "";
        List<InventoryUploadDto_Resp>  inventoryUploadDto_RespList = new ArrayList<>();

        try  {

            for (InventoryUploadDto_Req inventoryUploadDto_req : inventoryUploadDto_reqList) {

                    InventoryUploadDto_Resp inventoryUploadDto_resp = new InventoryUploadDto_Resp();
                    itemDescription = inventoryUploadDto_req.getInventoryName().toUpperCase().replaceAll(" ","");
                    inventoryUploadDto_resp.setName(inventoryUploadDto_req.getInventoryName());
                    inventoryUploadDto_resp.setConsumption(String.valueOf(inventoryUploadDto_req.getConsumption()));
                     consumption = inventoryUploadDto_req.getConsumption();
                    inventoryUploadDto_resp.setConsumptionUnit(inventoryUploadDto_req.getConsumptionUnit());

                        if(refrenceData != null && refrenceData.get(itemDescription) != null){

                            inventoryUploadDto_resp.setScope( refrenceData.get(itemDescription).split("~")[0]);
                            inventoryUploadDto_resp.setCategory( refrenceData.get(itemDescription).split("~")[1]);
                            inventoryUploadDto_resp.setStandard( refrenceData.get(itemDescription).split("~")[2]);
                            inventoryUploadDto_resp.setSubcategory( refrenceData.get(itemDescription).split("~")[3]);
                            inventoryUploadDto_resp.setFactor(refrenceData.get(itemDescription).split("~")[4]);
                            factor = Double.valueOf(refrenceData.get(itemDescription).split("~")[4]);
                            inventoryUploadDto_resp.setFactorDetail( refrenceData.get(itemDescription).split("~")[5]);
                            inventoryUploadDto_resp.setCategoryIndex(Long.valueOf(refrenceData.get(itemDescription).split("~")[6]));
                            inventoryUploadDto_resp.setStandardIndex(Long.valueOf(refrenceData.get(itemDescription).split("~")[7]));
                            inventoryUploadDto_resp.setSubcategoryIndex(Long.valueOf(refrenceData.get(itemDescription).split("~")[8]));
                            inventoryUploadDto_resp.setConsumptionUnit(refrenceData.get(itemDescription).split("~")[9]);
                            inventoryUploadDto_resp.setTco2(String.valueOf(consumption * factor));
                            inventoryUploadDto_resp.setTagIndex(null);
                            inventoryUploadDto_resp.setUsages("1");
                            inventoryUploadDto_resp.setUsageUnit(refrenceData.get(itemDescription).split("~")[9]);
                            inventoryUploadDto_resp.setGasIndex(1L);
                            inventoryUploadDto_resp.setGassName("CO2");
                            inventoryUploadDto_resp.setStatus("1");
                            inventoryUploadDto_resp.setDescription(null);
                        }
                inventoryUploadDto_resp.setActivity_index(inventoryUploadDto_req.getActivity_index());
                inventoryUploadDto_RespList.add(inventoryUploadDto_resp);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return inventoryUploadDto_RespList;
    }

    @Override
    @Transactional
    public void saveAllInventories(List<InventoryDTO_Req> inventoryDtoReqList) {
        int acceptedCount = 0;
        int rejectedCount = 0;
        Long emissionIndex = 0l;
        int loopRestricter = 0;


        InventoryUploadHistoryModel inventoryUploadHistoryModel = new InventoryUploadHistoryModel();
        List <InventoryModel> inventoryModelList = new ArrayList<>();

        for(InventoryDTO_Req inventoryDtoReq : inventoryDtoReqList){

            InventoryModel inventoryModel = modelMapper.map(inventoryDtoReq, InventoryModel.class);
            inventoryModel.setId(null);
            inventoryModel.setActivityIndex(getEA(inventoryDtoReq.getActivity_index()));
            emissionIndex = inventoryDtoReq.getActivity_index();

            if(inventoryDtoReq.getStatus() == null || inventoryDtoReq.getStatus().equals("-2")){
                rejectedCount++;
                inventoryModel.setStatus("-2");

            }else if(inventoryDtoReq.getStatus().equals("1")){
                acceptedCount++;
            }

            if(loopRestricter  == 0){

                loopRestricter++;
                inventoryUploadHistoryModel.setAcceptedInventories(acceptedCount);
                inventoryUploadHistoryModel.setRejectedInventories(rejectedCount);
                inventoryUploadHistoryModel.setCreatedBy(RequestInterceptor.UserId);
                inventoryUploadHistoryModel.setStatus("1");
                inventoryUploadHistoryModel.setEmissionIndex(getEA(emissionIndex));
                inventoryUploadHistoryRepo.save(inventoryUploadHistoryModel);

            }

            if(!inventoryModel.getStatus().equals("-2")){
                inventoryModel.setInventoryUploadHistoryModelIndex(null);
            }else{
                inventoryModel.setInventoryUploadHistoryModelIndex(inventoryUploadHistoryModel);
            }

            if(inventoryModel.getActivityIndex().getBaselineTotalEmission() != null){
                inventoryModel.setBaselineTco2(inventoryModel.getTco2());
//                EmissionAlterationHistoryModel emissionAlterationHistoryModel =  eaService.mentainEmissionAlterationHistory(inventoryModel.getActivityIndex(),String.valueOf(inventoryRepo.totalEmissionByEID(inventoryModel.getActivityIndex().getId())), String.valueOf(RequestInterceptor.UserId));
//                inventoryService.mentainInventoryAlterationHistory(inventoryModel,String.valueOf(inventoryModel.getTco2()), String.valueOf(RequestInterceptor.UserId),emissionAlterationHistoryModel);
            }

            inventoryRepo.save(inventoryModel);
//            inventoryModelList.add(inventoryModel);
            if(inventoryModel.getActivityIndex().getBaselineTotalEmission() != null){
                EmissionAlterationHistoryModel emissionAlterationHistoryModel =  eaService.mentainEmissionAlterationHistory(inventoryModel.getActivityIndex(),String.valueOf(inventoryRepo.totalEmissionByEID(inventoryModel.getActivityIndex().getId())), String.valueOf(RequestInterceptor.UserId));
                inventoryService.mentainInventoryAlterationHistory(inventoryModel,String.valueOf(inventoryModel.getTco2()), String.valueOf(RequestInterceptor.UserId),emissionAlterationHistoryModel);
            }
        }

        inventoryUploadHistoryModel.setAcceptedInventories(acceptedCount);
        inventoryUploadHistoryModel.setRejectedInventories(rejectedCount);
//        inventoryRepo.saveAll(inventoryModelList);
//
//        for(InventoryModel model3: inventoryModelList){
//            if(model3.getActivityIndex().getBaselineTotalEmission() != null){
//                EmissionAlterationHistoryModel emissionAlterationHistoryModel =  eaService.mentainEmissionAlterationHistory(model3.getActivityIndex(),String.valueOf(inventoryRepo.totalEmissionByEID(model3.getActivityIndex().getId())), String.valueOf(RequestInterceptor.UserId));
//                inventoryService.mentainInventoryAlterationHistory(model3,String.valueOf(model3.getTco2()), String.valueOf(RequestInterceptor.UserId),emissionAlterationHistoryModel);
//            }
//        }





//
//        String[] nameArray = new String[inventoryModelList.size()];
//        Long[] categoryIndexArray = new Long[inventoryModelList.size()];
//        Long[] subcategoryIndexArray = new Long[inventoryModelList.size()];
//        String[] factorArray = new String[inventoryModelList.size()];
//        String[] factorDetailArray = new String[inventoryModelList.size()];
//        String[] descriptionArray = new String[inventoryModelList.size()];
//        Long[] tagIndexArray = new Long[inventoryModelList.size()];
//        Long[] gasIndexArray = new Long[inventoryModelList.size()];
//        String[] consumptionArray = new String[inventoryModelList.size()];
//        String[] consumptionUnitArray = new String[inventoryModelList.size()];
//        String[] tco2Array = new String[inventoryModelList.size()];
//        String[] baselineTco2Array = new String[inventoryModelList.size()];
//        String[] statusArray = new String[inventoryModelList.size()];
//        ZonedDateTime[] createdDateArray = new ZonedDateTime[inventoryModelList.size()];
//        String[] createdByArray = new String[inventoryModelList.size()];
//        ZonedDateTime[] updatedDateArray = new ZonedDateTime[inventoryModelList.size()];
//        String[] updatedByArray = new String[inventoryModelList.size()];
//        Long[] standardIndexArray = new Long[inventoryModelList.size()];
//        String[] usagesArray = new String[inventoryModelList.size()];
//        String[] usageUnitArray = new String[inventoryModelList.size()];
//        Long[] activity_index = new Long[inventoryModelList.size()];
//        Long[] inventory_upload_history_index = new Long[inventoryModelList.size()];
//
//
//        int i = 0;
//        for(InventoryModel model : inventoryModelList){
//
//                    nameArray[i] =  ""+ model.getName() +"";
//            categoryIndexArray[i] =   model.getCategoryIndex();
//                    subcategoryIndexArray[i] =   model.getSubcategoryIndex();
//            factorArray[i] =   model.getFactor();
//                    factorDetailArray[i] =  ""+ model.getFactorDetail() +"";
//            descriptionArray[i] =  ""+ model.getDescription() +"";
//                    tagIndexArray[i] =   model.getTagIndex();
//            gasIndexArray[i] =   model.getGasIndex();
//                    consumptionArray[i] =   model.getConsumption();
//            consumptionUnitArray[i] =   model.getConsumptionUnit();
//                    tco2Array[i] =   model.getTco2();
//            baselineTco2Array[i] =   model.getBaselineTco2();
//                    statusArray[i] =   model.getStatus();
//            createdDateArray[i] =   model.getCreatedDate();
//                    createdByArray[i] =   model.getCreatedBy();
//            updatedDateArray[i] =   model.getUpdatedDate();
//                    updatedByArray[i] =   model.getUpdatedBy();
//            standardIndexArray[i] =   model.getStandardIndex();
//                    usagesArray[i] =   model.getUsages();
//            usageUnitArray[i] =   model.getUsageUnit();
//            activity_index[i] =   model.getActivityIndex().getId();
//                    inventory_upload_history_index[i] =   (model.getInventoryUploadHistoryModelIndex() == null) ? null :model.getInventoryUploadHistoryModelIndex().getId();
//            i++;
//        }



//        inventoryRepo.saveByInsetQuery(
//                nameArray,
//                categoryIndexArray,
//                subcategoryIndexArray,
//                factorArray,
//                factorDetailArray,
//                descriptionArray,
//                tagIndexArray,
//                gasIndexArray,
//                consumptionArray,
//                consumptionUnitArray,
//                tco2Array,
//                baselineTco2Array,
//                statusArray,
//                createdDateArray,
//                createdByArray,
//                updatedDateArray,
//                updatedByArray,
//                standardIndexArray,
//                usagesArray,
//                usageUnitArray,
//                activity_index,
//                inventory_upload_history_index
//        );

    }

    public EAModel getEA(Long eaId) {

        EAModel eaModel = eaRepo.findById(eaId).orElseThrow(() -> new ResourceNotFoundException("Emission assessment not found"));
        return eaModel;
    }

    public HashMap<String, String> getApiResponse() {
        try {
            HashMap<String, String> container = new HashMap<>();
            container = webclient
                    .build()
                    .get()
                    .uri(RefferenceDataURL)
                    .header(HttpHeaders.AUTHORIZATION, RequestInterceptor.Token,HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve().onStatus(HttpStatusCode::is4xxClientError, response-> Mono.error(new ResourceNotFoundException(""+RefferenceDataURL+" result is null")))
                    .bodyToMono(new ParameterizedTypeReference<HashMap<String, String>>() {
                    }).block();
            return container;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    static boolean NoValueFound(final String str) {
        return (str == null) || (str.length() <= 0);
    }
}
