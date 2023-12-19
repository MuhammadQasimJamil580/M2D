package com.M2D.EmissionAssessment.ServiceImpl;

import com.M2D.EmissionAssessment.DTO.Request.InventoryBulkUploadDtoReq;
import com.M2D.EmissionAssessment.DTO.Response.InventoryUploadDto_Resp;
import com.M2D.EmissionAssessment.Exception.ResourceNotFoundException;
import com.M2D.EmissionAssessment.MultiTenancy.RequestInterceptor;
import com.M2D.EmissionAssessment.Services.InventoryBulkUploadService;
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
import java.util.Map;

@Service
public class InventoryBulkUploadServiceImpl implements InventoryBulkUploadService {

    @Autowired
    private WebClient.Builder webclient;

    @Value("${RefferenceDataURL}")
    private String RefferenceDataURL;
    @Value("${tagURL}")
    private String tagURL;

    @Override
    public List<InventoryUploadDto_Resp> inventoryBulkUpload(List<InventoryBulkUploadDtoReq> inventoryBulkUploadDtoReqList) {
        String SubCategory = "";
        Double consumption = 0.00;
        Double factor = 0.00;
        List<InventoryUploadDto_Resp> InventoryUploadDto_RespList = new ArrayList<>();
        HashMap<String, String> GetData = getApiResponse(RefferenceDataURL);
        HashMap<Long, String> TagURL = gettag(tagURL);
        HashMap<String, Long> swappedMap = new HashMap<>();

        TagURL.forEach((key, value) -> {
            swappedMap.put(value, key);
        });

         for ( InventoryBulkUploadDtoReq inventoryBulkUploadDtoReq : inventoryBulkUploadDtoReqList ) {
             SubCategory = inventoryBulkUploadDtoReq.getMaterial().toUpperCase().replaceAll(" ", "");
             InventoryUploadDto_Resp inventoryUploadDto_resp = new InventoryUploadDto_Resp();
             consumption = Double.valueOf(inventoryBulkUploadDtoReq.getConsumption());
             if (GetData != null && GetData.get(SubCategory) != null) {
                 inventoryUploadDto_resp.setScope(GetData.get(SubCategory).split("~")[0]);
                 inventoryUploadDto_resp.setCategory(GetData.get(SubCategory).split("~")[1]);
                 inventoryUploadDto_resp.setStandard(GetData.get(SubCategory).split("~")[2]);
                 inventoryUploadDto_resp.setSubcategory(GetData.get(SubCategory).split("~")[3]);
                 inventoryUploadDto_resp.setFactor(GetData.get(SubCategory).split("~")[4]);
                 factor = Double.valueOf(GetData.get(SubCategory).split("~")[4]);
                 inventoryUploadDto_resp.setFactorDetail(GetData.get(SubCategory).split("~")[5]);
                 inventoryUploadDto_resp.setCategoryIndex(Long.valueOf(GetData.get(SubCategory).split("~")[6]));
                 inventoryUploadDto_resp.setStandardIndex(Long.valueOf(GetData.get(SubCategory).split("~")[7]));
                 inventoryUploadDto_resp.setSubcategoryIndex(Long.valueOf(GetData.get(SubCategory).split("~")[8]));
                 inventoryUploadDto_resp.setConsumptionUnit(GetData.get(SubCategory).split("~")[9]);
                 inventoryUploadDto_resp.setConsumption(String.valueOf(inventoryBulkUploadDtoReq.getConsumption()));
                 inventoryUploadDto_resp.setTco2(String.valueOf(consumption * factor));
                 inventoryUploadDto_resp.setTagIndex(null);
                 inventoryUploadDto_resp.setUsages(inventoryBulkUploadDtoReq.getUsage());
                 inventoryUploadDto_resp.setUsageUnit(GetData.get(SubCategory).split("~")[9]);
                 inventoryUploadDto_resp.setGasIndex(1L);
                 inventoryUploadDto_resp.setGassName("CO2");
                 inventoryUploadDto_resp.setName(inventoryBulkUploadDtoReq.getInventoryName());
                 inventoryUploadDto_resp.setStatus("1");
                 inventoryUploadDto_resp.setDescription(inventoryBulkUploadDtoReq.getInvDetail());
                inventoryUploadDto_resp.setTagIndex(swappedMap.get(inventoryBulkUploadDtoReq.getTagName()));
             }
             InventoryUploadDto_RespList.add(inventoryUploadDto_resp);
         }

        return InventoryUploadDto_RespList;
    }

    public HashMap<String, String> getApiResponse(String URL) {
        try {
            HashMap<String, String> container = new HashMap<>();
            container = webclient
                    .build()
                    .get()
                    .uri(URL)
                    .header(HttpHeaders.AUTHORIZATION, RequestInterceptor.Token, HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve().onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new ResourceNotFoundException("" + URL + " result is null")))
                    .bodyToMono(new ParameterizedTypeReference<HashMap<String, String>>() {
                    }).block();
            return container;
        } catch (Exception e) {
            return null;
        }
    }

    public HashMap<Long, String> gettag(String URL) {
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




}
