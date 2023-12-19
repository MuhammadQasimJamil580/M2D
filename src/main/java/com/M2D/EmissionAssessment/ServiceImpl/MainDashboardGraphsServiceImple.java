package com.M2D.EmissionAssessment.ServiceImpl;

import com.M2D.EmissionAssessment.DTO.General.PeriodDtoForGraph_Resp;
import com.M2D.EmissionAssessment.DTO.General.ScopeWiseCategoryIndexListDto;
import com.M2D.EmissionAssessment.DTO.Request.EADTO_Req;
import com.M2D.EmissionAssessment.DTO.Response.HistoricalEmissionsGraphDto;
import com.M2D.EmissionAssessment.DTO.Response.ScopeAndTagwiseEmission;
import com.M2D.EmissionAssessment.DTO.Response.UseTagToGetEmissionGraphDto;
import com.M2D.EmissionAssessment.Exception.ResourceNotFoundException;
import com.M2D.EmissionAssessment.Model.EAModel;
import com.M2D.EmissionAssessment.MultiTenancy.RequestInterceptor;
import com.M2D.EmissionAssessment.Repository.EARepo;
import com.M2D.EmissionAssessment.Repository.InventoryRepo;
import com.M2D.EmissionAssessment.Services.MainDashboardGraphsService;
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
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class MainDashboardGraphsServiceImple implements MainDashboardGraphsService {

    @Autowired
    private EARepo eaRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private WebClient.Builder webclient;
    @Value("${categoryURL2}")
    private String categoryURL2;

    @Value("${periodURL2}")
    private String PeriodURL2;
    @Value("${tagURL}")
    private String tagURL;


    @Override
    public ScopeAndTagwiseEmission getFilteredDataForScopeWiseGraph(Long[] siteIndexes, Long[] periodIndexes) {


        HashMap<Long, String> tagMap = getApiResponse(tagURL, new ParameterizedTypeReference<HashMap<Long, String>>() {});
        ScopeAndTagwiseEmission scopeAndTagwiseEmission = new ScopeAndTagwiseEmission();
        ScopeWiseCategoryIndexListDto scopeWiseCategoryIndexListDto = getApiResponse(categoryURL2, new ParameterizedTypeReference<ScopeWiseCategoryIndexListDto>() {});
                List<Object[]> result1 = new ArrayList<>();
                List<Object[]> result2 = new ArrayList<>();
                List<Object[]> result3 = new ArrayList<>();
                List<Object[]> result4 = new ArrayList<>();

        String[] array1 = scopeWiseCategoryIndexListDto.getScope1CategoryIndexes();
        String[] array2 = scopeWiseCategoryIndexListDto.getScope2CategoryIndexes();
        String[] array3 = scopeWiseCategoryIndexListDto.getScope3CategoryIndexes();

        int length1 = array1.length;
        int length2 = array2.length;
        int length3 = array3.length;

        String[] allCategoryIndexes = new String[length1 + length2 + length3];

        System.arraycopy(array1, 0, allCategoryIndexes, 0, length1);
        System.arraycopy(array2, 0, allCategoryIndexes, length1, length2);
        System.arraycopy(array3, 0, allCategoryIndexes, length1 + length2, length3);


        if(siteIndexes == null && periodIndexes == null){
            result1 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWise(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes());
            result2 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWise(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes());
            result3 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWise(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes());
            result4 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWise(allCategoryIndexes);
        }

        if(siteIndexes != null && periodIndexes != null){

            Long[] emissionIndexesArray = eaRepo.getEmissionIndexesAgainstSiteAndPeriod(siteIndexes,periodIndexes);
             result1 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes(),emissionIndexesArray);
             result2 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes(),emissionIndexesArray);
             result3 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes(),emissionIndexesArray);
             result4 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(allCategoryIndexes,emissionIndexesArray);
        }else if(siteIndexes != null && periodIndexes == null){

            Long[] emissionIndexesArray = eaRepo.getEmissionIndexesAgainstSite(siteIndexes);
            result1 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes(),emissionIndexesArray);
            result2 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes(),emissionIndexesArray);
            result3 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes(),emissionIndexesArray);
            result4 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(allCategoryIndexes,emissionIndexesArray);

        }else if(siteIndexes == null && periodIndexes != null){

            Long[] emissionIndexesArray = eaRepo.getEmissionIndexesAgainstPeriod(periodIndexes);
            result1 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes(),emissionIndexesArray);
            result2 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes(),emissionIndexesArray);
            result3 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes(),emissionIndexesArray);
            result4 =  inventoryRepo.getTagIndexAndTotalEmissionScopeWiseAndEmissionWise(allCategoryIndexes,emissionIndexesArray);

        }

        List<Double> scope1Emission = getDataFromListOfObjects( result1, 1, tagMap  ,0);
        List<Double> scope2Emission = getDataFromListOfObjects( result2, 1, tagMap  ,0);
        List<Double> scope3Emission = getDataFromListOfObjects( result3, 1, tagMap  ,0);
        List<Double> totalEmission = getDataFromListOfObjects( result4, 1, tagMap  ,0);
        List<String> tagNames = getDataFromListOfObjects( result4, 0, tagMap  ,1);
        List<Integer> IndexesOfNullValue =  findIndex(tagNames, "N/A");

        if(!IndexesOfNullValue.isEmpty()){
            for(int x : IndexesOfNullValue){
                scope1Emission.remove(x);
                scope2Emission.remove(x);
                scope3Emission.remove(x);
                totalEmission.remove( x);
                tagNames.remove(x);
            }
        }

        scopeAndTagwiseEmission.setScope1Emissions(scope1Emission);
        scopeAndTagwiseEmission.setScope2Emissions(scope2Emission);
        scopeAndTagwiseEmission.setScope3Emissions(scope3Emission);
        scopeAndTagwiseEmission.setTotalEmissions(totalEmission);
        scopeAndTagwiseEmission.setTagNames(tagNames);
        return scopeAndTagwiseEmission;
    }



    @Override
    public HistoricalEmissionsGraphDto getFilteredDataForHistoricEmissions(Long[] siteIndexes, Long[] periodIndexes) {


        List<Double> result1 = new ArrayList<>();
        List<Double> result2 = new ArrayList<>();
        List<Double> result3 = new ArrayList<>();
        Long[] emissionIndexesArray = null;
        List<String> periodNames = new ArrayList<>();
        HistoricalEmissionsGraphDto historicalEmissionsGraphDto = new HistoricalEmissionsGraphDto();
        ScopeAndTagwiseEmission scopeAndTagwiseEmission = new ScopeAndTagwiseEmission();
        ScopeWiseCategoryIndexListDto scopeWiseCategoryIndexListDto = getApiResponse(categoryURL2, new ParameterizedTypeReference<ScopeWiseCategoryIndexListDto>() {});
        List<PeriodDtoForGraph_Resp> periodDtoForGraph_respsList  = getApiResponse(PeriodURL2, new ParameterizedTypeReference<List<PeriodDtoForGraph_Resp>>() {});
        LinkedHashSet<String> periodset = new LinkedHashSet<>();
        HashMap<Long, String> periodMapToGetPeriodNames = new HashMap<>();
        HashMap<String, Long[]> periodMapToGetPeriodIndexes = new HashMap<>();
        for (PeriodDtoForGraph_Resp dto : periodDtoForGraph_respsList) {
            Long[] ids = dto.getId();
            String periodName = dto.getPeriodName();
            for (Long id : ids) {
                periodMapToGetPeriodNames.put(id, periodName);
            }
            periodMapToGetPeriodIndexes.put(periodName,ids);
        }

            if(siteIndexes == null && periodIndexes == null){
                for(PeriodDtoForGraph_Resp periodDto : periodDtoForGraph_respsList) {
                    periodNames.add(periodDto.getPeriodName());
                    emissionIndexesArray = eaRepo.getEmissionIndexesAgainstPeriod(periodDto.getId());
                    result1.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes(),emissionIndexesArray) );
                    result2.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes(),emissionIndexesArray) );
                    result3.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes(),emissionIndexesArray) );
                }

            }
            if(siteIndexes != null && periodIndexes != null){

                for(Long x: periodIndexes){
                    periodset.add(periodMapToGetPeriodNames.get(x));
                }
                for (String y : periodset){
                    emissionIndexesArray = eaRepo.getEmissionIndexesAgainstSiteAndPeriod(siteIndexes,periodMapToGetPeriodIndexes.get(y));
                    result1.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes(),emissionIndexesArray) );
                    result2.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes(),emissionIndexesArray) );
                    result3.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes(),emissionIndexesArray) );
                }

                List<String> list1 = new ArrayList<String>(periodset);
                periodNames = list1;

            }else if(siteIndexes != null){

                for(PeriodDtoForGraph_Resp periodDto : periodDtoForGraph_respsList) {
                    periodNames.add(periodDto.getPeriodName());
                    emissionIndexesArray = eaRepo.getEmissionIndexesAgainstSiteAndPeriod(siteIndexes,periodDto.getId());
                    result1.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes(),emissionIndexesArray) );
                    result2.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes(),emissionIndexesArray) );
                    result3.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes(),emissionIndexesArray) );
                }

            }else if(periodIndexes != null){

                for(Long x: periodIndexes){
                    periodset.add(periodMapToGetPeriodNames.get(x));
                }

                for (String y : periodset){
                    emissionIndexesArray = eaRepo.getEmissionIndexesAgainstPeriod(periodMapToGetPeriodIndexes.get(y));
                    result1.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope1CategoryIndexes(),emissionIndexesArray) );
                    result2.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope2CategoryIndexes(),emissionIndexesArray) );
                    result3.add( (inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes(),emissionIndexesArray)) == null ? 0 : inventoryRepo.getHistoricEmissions(scopeWiseCategoryIndexListDto.getScope3CategoryIndexes(),emissionIndexesArray) );
                }
                List<String> list1 = new ArrayList<String>(periodset);
                periodNames = list1;
            }

        historicalEmissionsGraphDto.setPeriodNames(periodNames);
        historicalEmissionsGraphDto.setScope1Emissions(result1);
        historicalEmissionsGraphDto.setScope2Emissions(result2);
        historicalEmissionsGraphDto.setScope3Emissions(result3);
        return historicalEmissionsGraphDto;
    }


    @Override
    public List<UseTagToGetEmissionGraphDto> findAllTCO2ThroughTag() {

        HashMap<Long, String> TagList = getApiResponse(tagURL, new ParameterizedTypeReference<HashMap<Long, String>>() {});

        List<Object[]> inventoryModelList = inventoryRepo.alltags();
        List<UseTagToGetEmissionGraphDto> useTagToGetEmissionGraphDtoList =
                new ArrayList<>(inventoryModelList.size());
        for (Object[] inventoryModel : inventoryModelList) {
            Long TagIndex = ((Number) inventoryModel[0]).longValue();
            double Tco2 = ((Number) inventoryModel[1]).doubleValue();

            UseTagToGetEmissionGraphDto useTagToGetEmissionGraphDto = new UseTagToGetEmissionGraphDto();
            if (TagList.get(TagIndex) != null) {
                useTagToGetEmissionGraphDto.setTCO2((Tco2));
                useTagToGetEmissionGraphDto.setTagName(TagList.get(TagIndex));
                useTagToGetEmissionGraphDtoList.
                        add(useTagToGetEmissionGraphDto);
            }
        }
        return useTagToGetEmissionGraphDtoList;
    }

    public List<UseTagToGetEmissionGraphDto> findAllTCo2ThroughOrgSitAndPeriod(Long[] orgSiteIndex, Long[] PeriodIndex) {
        List<UseTagToGetEmissionGraphDto> useTagToGetEmissionGraphDtoList = new ArrayList<>();
        HashMap<Long, String> TagList = getApiResponse(tagURL, new ParameterizedTypeReference<HashMap<Long, String>>() {});
        int i = 0;
        List<EAModel> eaModelList = eaRepo.getEmissionAssessments(orgSiteIndex, PeriodIndex);
        Long[] ActivityIndex = new Long[eaModelList.size()];
        List<EADTO_Req> eaDtoReqList = new ArrayList<>();
        for (EAModel eaModel : eaModelList) {
            EADTO_Req eaDtoReq = new EADTO_Req();
            BeanUtils.copyProperties(eaModel, eaDtoReq);
            eaDtoReqList.add(eaDtoReq);
            ActivityIndex[i] = eaModel.getId();
            i++;
            List<Object[]> inventoryModelList = inventoryRepo.allTagsThroughOrgAndPeriodIndex(eaModel.getId());
            for (Object[] inventoryModel : inventoryModelList) {
                UseTagToGetEmissionGraphDto useTagToGetEmissionGraph = new UseTagToGetEmissionGraphDto();
                Long TagIndex = ((Number) inventoryModel[0]).longValue();
                double Tco2 = ((Number) inventoryModel[1]).doubleValue();

                if (TagList.get(TagIndex) != null) {
                    useTagToGetEmissionGraph.setTCO2((Tco2));
                    useTagToGetEmissionGraph.setTagName(TagList.get(TagIndex));
                    useTagToGetEmissionGraphDtoList.add(useTagToGetEmissionGraph);
                }

            }
        }
        return useTagToGetEmissionGraphDtoList;
    }



    public <T> T getApiResponse(String URL, ParameterizedTypeReference<T> responseType) {
        try {

            T container = webclient
                    .build()
                    .get()
                    .uri(URL)
                    .header(HttpHeaders.AUTHORIZATION, RequestInterceptor.Token,HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve().onStatus(HttpStatusCode::is4xxClientError, response-> Mono.error(new ResourceNotFoundException(""+URL+" result is null")))
                    .bodyToMono(responseType).block();
            return container;
        } catch (Exception e) {
            return null;
        }
    }

    public <T> List<T> getDataFromListOfObjects(List<Object[]> result, Integer rowindex, HashMap<Long, String> map , Integer flag){
        int i = 0;
        String x = "";
        List<T> data = new ArrayList<>();
        for (Object[] row : result) {
            if(flag == 1){
                x = (map.get(((Integer)row[rowindex]).longValue())) == null ? "N/A" : map.get(((Integer)row[rowindex]).longValue());
                data.add((T) x);
            }else{
                data.add( ((T) row[rowindex]));
            }
            i++;
        }
        return data;
    }


    public  List<Integer> findIndex(List<String> list, String targetValue) {
        List<Integer> indexesOfNullValue = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(targetValue)) {
                indexesOfNullValue.add(i);
            }
        }
        return indexesOfNullValue;
    }

}
