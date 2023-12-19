package com.M2D.EmissionAssessment.ServiceImpl;

import com.M2D.EmissionAssessment.DTO.General.CategoryByScopeDTO;
import com.M2D.EmissionAssessment.DTO.Request.InventoryUploadHistoryReqDto;
import com.M2D.EmissionAssessment.DTO.Response.EADtoUploadHistoryResp;
import com.M2D.EmissionAssessment.DTO.Response.InventoryDTO_Resp;
import com.M2D.EmissionAssessment.DTO.Response.InventoryUploadHistoryRespDto;
import com.M2D.EmissionAssessment.Exception.ResourceNotFoundException;
import com.M2D.EmissionAssessment.Model.EAModel;
import com.M2D.EmissionAssessment.Model.InventoryModel;
import com.M2D.EmissionAssessment.Model.InventoryUploadHistoryModel;
import com.M2D.EmissionAssessment.MultiTenancy.RequestInterceptor;
import com.M2D.EmissionAssessment.Repository.EARepo;
import com.M2D.EmissionAssessment.Repository.InventoryRepo;
import com.M2D.EmissionAssessment.Repository.InventoryUploadHistoryRepo;
import com.M2D.EmissionAssessment.Services.InventoryUploadHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryUploadHistoryServiceImp implements InventoryUploadHistoryService {
    @Autowired
    InventoryUploadHistoryRepo historyRepo;
    HashMap<Long, String> userMap = new HashMap<>();
    private static final WebClient webClient = WebClient.create();

    @Autowired
    EARepo eaRepo;

    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WebClient.Builder webclient;
    @Value("${userHashMap}")
    private String userHashMap;
    @Value("${categoryURL}")
    private String categoryURL;
    @Value("${subCategoryURL}")
    private String subCategoryURL;
    @Value("${standardURL}")
    private String standardURL;
    @Value("${tagURL}")
    private String tagURL;
    @Value("${gassURL}")
    private String gassURL;
    @Value("${periodURL}")
    private String periodURL;


    @Override
    public List<InventoryDTO_Resp> getAllScanned(Long eaId) {

        HashMap<Long, String> categoryAndScopeMap = getApiResponse(categoryURL);
        HashMap<Long, String> SubcategoryMap = getApiResponse(subCategoryURL);
        HashMap<Long, String> standardMap = getApiResponse(standardURL);
        HashMap<Long, String> tagMap = getApiResponse(tagURL);
        HashMap<Long, String> gassMap = getApiResponse(gassURL);

        InventoryUploadHistoryModel model = new InventoryUploadHistoryModel();
        model.setId(eaId);
        List<InventoryModel> inventoryModels = inventoryRepo.findInventoryModelByInventoryUploadHistoryModelIndexAndStatus(model, "-2");

        try {
            if (inventoryModels.isEmpty() || inventoryModels == null) {
                throw new ResourceNotFoundException("Inventory not found");
            } else {
                List<InventoryDTO_Resp> inventoryDTO_resps = inventoryModels.stream().map(x -> mapInventoryDto(x, categoryAndScopeMap, SubcategoryMap, standardMap, tagMap, gassMap)).collect(Collectors.toList());
                return inventoryDTO_resps;
            }
        } catch (Exception e) {

            System.out.println(e);
        }
        return null;


    }

    @Override
    public List<InventoryUploadHistoryRespDto> getAll() {

        List<InventoryUploadHistoryModel> inventoryHistoryEntities = historyRepo.findByStatusNot("-1");
        List<InventoryUploadHistoryRespDto> inventoryHistoryDtos = new ArrayList<>(inventoryHistoryEntities.size());
        try {
            getData();
        } catch (Exception e) {
            //email
            throw new ResourceNotFoundException("User API is not working");
        }
        inventoryHistoryEntities.forEach(inventoryUploadHistoryModel -> {

            InventoryUploadHistoryRespDto inventoryUploadHistoryRespDto = new InventoryUploadHistoryRespDto();
            BeanUtils.copyProperties(inventoryUploadHistoryModel, inventoryUploadHistoryRespDto);

            EADtoUploadHistoryResp EArespDto = new EADtoUploadHistoryResp();
            BeanUtils.copyProperties(inventoryUploadHistoryModel.getEmissionIndex(), EArespDto);


            inventoryUploadHistoryRespDto.setCreatedBy(userMap.get(inventoryUploadHistoryModel.getCreatedBy()));
            inventoryUploadHistoryRespDto.setEmission_index(EArespDto);

            inventoryHistoryDtos.add(inventoryUploadHistoryRespDto);
        });
        return inventoryHistoryDtos;
    }

    @Override
    public List<InventoryUploadHistoryRespDto> getByEmissionIndex(Long emissionIndex) {

        EAModel eaModel = new EAModel();
        eaModel.setId(emissionIndex);
        List<InventoryUploadHistoryModel> inventoryHistoryEntities = historyRepo.findInventoryUploadHistoryModelByEmissionIndexAndStatusNot(eaModel, "-1");
        List<InventoryUploadHistoryRespDto> inventoryHistoryDtos = new ArrayList<>(inventoryHistoryEntities.size());

        inventoryHistoryEntities.forEach(inventoryUploadHistoryModel -> {
            InventoryUploadHistoryRespDto inventoryUploadHistoryRespDto = new InventoryUploadHistoryRespDto();
            BeanUtils.copyProperties(inventoryUploadHistoryModel, inventoryUploadHistoryRespDto);

            EADtoUploadHistoryResp EArespDto = new EADtoUploadHistoryResp();
            BeanUtils.copyProperties(inventoryUploadHistoryModel.getEmissionIndex(), EArespDto);
            inventoryUploadHistoryRespDto.setEmission_index(EArespDto);


            inventoryHistoryDtos.add(inventoryUploadHistoryRespDto);
        });
        return inventoryHistoryDtos;

    }

    @Override
    public String saveInventoryHistory(InventoryUploadHistoryReqDto inventoryUploadHistoryReqDto) {


        InventoryUploadHistoryModel inventoryModel = new InventoryUploadHistoryModel();
        EAModel eaModel = eaRepo.findByIdAndStatusNot(inventoryUploadHistoryReqDto.getEmission_index().getId(), "-1");
        if (eaModel == null) {
            return "Emission is not available";
        } else {
            inventoryModel.setEmissionIndex(eaModel);
            BeanUtils.copyProperties(inventoryUploadHistoryReqDto, inventoryModel);

            historyRepo.save(inventoryModel);
            return "Saved sucessfully";
        }


    }

    @Override
    public String updateInventoryHistory(InventoryUploadHistoryReqDto inventoryUploadHistoryReqDto) {
        InventoryUploadHistoryModel inventoryModel = historyRepo.findByIdAndStatusNot(inventoryUploadHistoryReqDto.getId(), "-1");
        if (inventoryModel == null) {
            return "History doesn't exist";
        } else {
            EAModel eaModel = eaRepo.findByIdAndStatusNot(inventoryUploadHistoryReqDto.getEmission_index().getId(), "-1");
            if (eaModel == null) {
                return "Emission is not available";
            } else {
                inventoryModel.setEmissionIndex(eaModel);
                BeanUtils.copyProperties(inventoryUploadHistoryReqDto, inventoryModel);

                historyRepo.save(inventoryModel);
                return "Saved sucessfully";
            }
        }

    }


    @Override
    public String deleteInventoryHistory(Long id) {
        return null;
    }

    public HashMap<Long, String> getApiResponse(String URL) {
        try {
            HashMap<Long, String> container = new HashMap<>();
            container = webclient
                    .build()
                    .get()
                    .uri(URL)
                    .header(HttpHeaders.AUTHORIZATION, RequestInterceptor.Token, HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve().onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new ResourceNotFoundException("" + URL + " result is null")))
                    .bodyToMono(new ParameterizedTypeReference<HashMap<Long, String>>() {
                    }).block();
            return container;
        } catch (Exception e) {
            return null;
        }
    }


    public InventoryDTO_Resp mapInventoryDto(InventoryModel inventoryModel, HashMap<Long, String> categoryAndScopeMap, HashMap<Long, String> SubcategoryMap, HashMap<Long, String> standardMap, HashMap<Long, String> tagMap, HashMap<Long, String> gassMap) {
        InventoryDTO_Resp inventoryDTO_resp = modelMapper.map(inventoryModel, InventoryDTO_Resp.class);
        Long categoryIndex = inventoryModel.getCategoryIndex();
        Long subcategoryIndex = inventoryModel.getSubcategoryIndex();
        Long standardIndex = inventoryModel.getStandardIndex();
        Long tagIndex = inventoryModel.getTagIndex();
        Long gasIndex = inventoryModel.getGasIndex();
        inventoryDTO_resp.setCategoryname((categoryIndex != null && categoryAndScopeMap != null) ? (categoryAndScopeMap.get(categoryIndex) != null) ? categoryAndScopeMap.get(categoryIndex).split("~")[1] : "N/A" : "N/A");
        inventoryDTO_resp.setScope((categoryIndex != null && categoryAndScopeMap != null) ? (categoryAndScopeMap.get(categoryIndex) != null) ? categoryAndScopeMap.get(categoryIndex).split("~")[0] : "N/A" : "N/A");
        inventoryDTO_resp.setSubCategoryname((subcategoryIndex != null && SubcategoryMap != null) ? (SubcategoryMap.get(subcategoryIndex) != null) ? SubcategoryMap.get(subcategoryIndex) : "N/A" : "N/A");
        inventoryDTO_resp.setStandardName((standardIndex != null && standardMap != null) ? (standardMap.get(standardIndex) != null) ? standardMap.get(standardIndex) : "N/A" : "N/A");
        inventoryDTO_resp.setTagName((tagIndex != null && tagMap != null) ? (tagMap.get(tagIndex) != null) ? tagMap.get(tagIndex) : "N/A" : "N/A");
        inventoryDTO_resp.setGassName((gasIndex != null && gassMap != null) ? (gassMap.get(gasIndex) != null) ? gassMap.get(gasIndex) : "N/A" : "N/A");
        return inventoryDTO_resp;
    }

    public HashMap<Long, String> getData() {

        userMap = webClient.get()
                .uri(userHashMap)
                .header(HttpHeaders.AUTHORIZATION, RequestInterceptor.Token, HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
//                .bodyToMono(HashMap.class).block();
                .bodyToMono(new ParameterizedTypeReference<HashMap<Long, String>>() {
                }).block();

        return userMap;
    }

}
