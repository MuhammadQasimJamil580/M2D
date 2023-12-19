package com.M2D.EmissionAssessment.ServiceImpl;

import com.M2D.EmissionAssessment.DTO.General.InventoryDataForRIDTO;
import com.M2D.EmissionAssessment.DTO.Request.InventoryDTO_Req;
import com.M2D.EmissionAssessment.DTO.Response.InventoryDTO_Resp;
import com.M2D.EmissionAssessment.Exception.ResourceNotFoundException;
import com.M2D.EmissionAssessment.Model.*;
import com.M2D.EmissionAssessment.Model.EAModel;
import com.M2D.EmissionAssessment.Model.InventoryModel;
import com.M2D.EmissionAssessment.Model.InventoryUploadHistoryModel;
import com.M2D.EmissionAssessment.MultiTenancy.RequestInterceptor;
import com.M2D.EmissionAssessment.Repository.*;
import com.M2D.EmissionAssessment.Services.EAService;
import com.M2D.EmissionAssessment.Services.InventoryService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryUploadHistoryRepo historyRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private EARepo eaRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WebClient.Builder webclient;
    @Autowired
    private InventoryAlterationHistoryRepo inventoryAlterationHistoryRepo;
    @Autowired
    private EAService eaService;
    @Autowired
    JdbcTemplate jdbcTemplate;

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
    public List<InventoryDTO_Resp> getAll(Long eId) {
        String[] statuslist = {"1"};
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Future<HashMap<Long, String>> categoryFuture = executor.submit(() -> getApiResponse(categoryURL));
        Future<HashMap<Long, String>> subcategoryFuture = executor.submit(() -> getApiResponse(subCategoryURL));
        Future<HashMap<Long, String>> standardFuture = executor.submit(() -> getApiResponse(standardURL));
        Future<HashMap<Long, String>> tagFuture = executor.submit(() -> getApiResponse(tagURL));
        Future<HashMap<Long, String>> gassFuture = executor.submit(() -> getApiResponse(gassURL));
        List<InventoryModel> inventoryModels = inventoryRepo.findByActivityIndexAndStatusIn(eId, statuslist).orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        try {

            HashMap<Long, String> categoryAndScopeMap =   categoryFuture.get();
            HashMap<Long, String> subcategoryMap =   subcategoryFuture.get();
            HashMap<Long, String> standardMap =   standardFuture.get();
            HashMap<Long, String> tagMap =   tagFuture.get();
            HashMap<Long, String> gassMap =   gassFuture.get();
            executor.shutdown();

            if (inventoryModels.isEmpty() || inventoryModels == null) {
                throw new ResourceNotFoundException("Inventory not found");
            } else {
                List<InventoryDTO_Resp> inventoryDTO_resps = inventoryModels.stream().map(x -> mapInventoryDto(x, categoryAndScopeMap, subcategoryMap, standardMap, tagMap, gassMap)).collect(Collectors.toList());
                return inventoryDTO_resps;
            }
        } catch (Exception e) {

            System.out.println(e);
        }
        return null;


    }

    @Override
    public InventoryDTO_Req getOneRecord(Long invId) {
        HashMap<Long, String> categoryAndScopeMap = getApiResponse(categoryURL);
        InventoryModel inventoryModel = inventoryRepo.findById(invId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        InventoryDTO_Req inventoryDTO_req = modelMapper.map(inventoryModel, InventoryDTO_Req.class);
        inventoryDTO_req.setScope(     (inventoryModel.getCategoryIndex() == null) ? null : categoryAndScopeMap.get(inventoryModel.getCategoryIndex()).split("~")[0]  );
        EAModel eaModel = inventoryModel.getActivityIndex();
        inventoryDTO_req.setActivity_index(eaModel.getId());
        return inventoryDTO_req;
    }

    @Override
    @Transactional
    public void saveInventory(InventoryDTO_Req inventoryDTO_req) {

        InventoryModel inventoryModel = modelMapper.map(inventoryDTO_req, InventoryModel.class);
        inventoryModel.setId(null);
        inventoryModel.setActivityIndex(getEA(inventoryDTO_req.getActivity_index()));

        /**
          If we are adding a new inv and its parent
         is baselined then in that case it will automatically
         be baselined and metain its alteration history
         */

        if(!NoValueFound(inventoryModel.getActivityIndex().getBaselineTotalEmission() )){
            inventoryModel.setBaselineTco2(inventoryModel.getTco2());
       }
        inventoryRepo.save(inventoryModel);

        if(!NoValueFound(inventoryModel.getActivityIndex().getBaselineTotalEmission() )){
            EmissionAlterationHistoryModel emissionAlterationHistoryModel =  eaService.mentainEmissionAlterationHistory(inventoryModel.getActivityIndex(),String.valueOf(inventoryRepo.totalEmissionByEID(inventoryModel.getActivityIndex().getId())), String.valueOf(RequestInterceptor.UserId));
            mentainInventoryAlterationHistory(inventoryModel,String.valueOf(inventoryModel.getTco2()), String.valueOf(RequestInterceptor.UserId), emissionAlterationHistoryModel);
        }
    }

    @Override
    @Transactional
    public void updateInventory(InventoryDTO_Req inventoryDTO_req, Long invId) {

        InventoryModel inventoryModel = inventoryRepo.findById(invId).orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        if (inventoryModel == null) {
            throw new ResourceNotFoundException("Inventory not Found");
        }
        modelMapper.map(inventoryDTO_req, inventoryModel);
        /**
         * handling the rejected to accepted case
         * where foreign key in inventory table is not null
         */

        if (inventoryModel.getInventoryUploadHistoryModelIndex() != null) {
            InventoryUploadHistoryModel historyModel = historyRepo.findByIdAndStatusNot(inventoryModel.getInventoryUploadHistoryModelIndex().getId(), "-1");
            if (historyModel != null) {
                historyModel.setAcceptedInventories(historyModel.getAcceptedInventories() + 1);
                historyModel.setRejectedInventories(historyModel.getRejectedInventories() - 1);
                historyRepo.save(historyModel);
            }
            inventoryModel.setInventoryUploadHistoryModelIndex(null);
            
            /**
             If we are updating an inv which is rejected and its parent
             is baselined then in that case it will automatically
             be baselined and metain its alteration history
             */

            if(!NoValueFound(inventoryModel.getActivityIndex().getBaselineTotalEmission())){
                inventoryModel.setBaselineTco2(inventoryModel.getTco2());
            }

        }

        inventoryModel.setId(invId);
        inventoryModel.setActivityIndex(modelMapper.map(getEA(inventoryDTO_req.getActivity_index()), EAModel.class));

        inventoryRepo.save(inventoryModel);
        if(!NoValueFound(inventoryModel.getActivityIndex().getBaselineTotalEmission() )){
            EmissionAlterationHistoryModel emissionAlterationHistoryModel =  eaService.mentainEmissionAlterationHistory(inventoryModel.getActivityIndex(),String.valueOf(inventoryRepo.totalEmissionByEID(inventoryModel.getActivityIndex().getId())), String.valueOf(RequestInterceptor.UserId));
            mentainInventoryAlterationHistory(inventoryModel,String.valueOf(inventoryModel.getTco2()), String.valueOf(RequestInterceptor.UserId),emissionAlterationHistoryModel);
        }

    }

    /**
     * @param invId
     */
    @Override
    @Transactional
    public void deleteInventory(Long invId) {
        InventoryModel inventoryModel = inventoryRepo.findById(invId).orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

        if (inventoryModel != null) {
            if (inventoryModel.getStatus().equals("-2")) {
                InventoryUploadHistoryModel historyModel = historyRepo.findByIdAndStatusNot(inventoryModel.getInventoryUploadHistoryModelIndex().getId(), "-1");
                if (historyModel != null) {
                    historyModel.setRejectedInventories(historyModel.getRejectedInventories() - 1);
                    historyRepo.save(historyModel);
                    inventoryRepo.deleteById(invId);
                    if(!NoValueFound(inventoryModel.getActivityIndex().getBaselineTotalEmission() )){
                        EmissionAlterationHistoryModel emissionAlterationHistoryModel =  eaService.mentainEmissionAlterationHistory(inventoryModel.getActivityIndex(),String.valueOf(inventoryRepo.totalEmissionByEID(inventoryModel.getActivityIndex().getId())), String.valueOf(RequestInterceptor.UserId));
                        mentainInventoryAlterationHistory(inventoryModel,String.valueOf(inventoryModel.getTco2()), String.valueOf(RequestInterceptor.UserId),emissionAlterationHistoryModel);
                    }
                } else {
                    throw new ResourceNotFoundException("Not available");
                }
            } else {
                inventoryModel.setStatus("-1");
                inventoryRepo.save(inventoryModel);
            }
        } else {
            throw new ResourceNotFoundException("Inventory didn't exist");
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

    public HashMap<Long, String> getApiResponse(String URL) {
        try {
            HashMap<Long, String> container = new HashMap<>();
            container = webclient
                    .build()
                    .get()
                    .uri(URL)
                    .header(HttpHeaders.AUTHORIZATION, RequestInterceptor.Token,HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve().onStatus(HttpStatusCode::is4xxClientError, response-> Mono.error(new ResourceNotFoundException(""+URL+" result is null")))
                    .bodyToMono(new ParameterizedTypeReference<HashMap<Long, String>>() {
                    }).block();
            return container;
        } catch (Exception e) {
            return null;
        }
    }

    public EAModel getEA(Long eaId) {

        EAModel eaModel = eaRepo.findById(eaId).orElseThrow(() -> new ResourceNotFoundException("Emission assessment not found"));
        return eaModel;
    }
    public InventoryServiceImpl(InventoryRepo inventoryRepo, EARepo eaRepo, ModelMapper modelMapper, WebClient.Builder webclient) {
        this.inventoryRepo = inventoryRepo;
        this.eaRepo = eaRepo;
        this.modelMapper = modelMapper;
        this.webclient = webclient;
    }
    @Override
    public HashMap<Long,String> getHMPOfInventorForRI() {

        HashMap<Long, String> periodMap = getApiResponse(periodURL);
        HashMap<Long, String> scopeMap = getApiResponse(categoryURL);
        HashMap<Long , String> inventoryMapInner = new HashMap<Long, String>();
        List<Object[]> result = inventoryRepo.findByStatusIsNot("-1 || -2").orElseThrow(() -> new ResourceNotFoundException("Not found"));
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Not found");
            }
        for (Object[] row : result) {
            Long inventory_index = ((Number) row[0]).longValue();
            Long activity_index_index = ((Number) row[1]).longValue();
            String name = NoValueFound(((String) row[2])) ? "N/A " : ((String) row[2]) ;
            String tco2 = NoValueFound(((String) row[3])) ? "N/A " : ((String) row[3]) ;
            Long category_index = ((Number) row[4]).longValue();
            Long period_index = ((Number) row[5]).longValue();
            inventoryMapInner.put(inventory_index,""+name+"~"+tco2+"~"+scopeMap.get(category_index).split("~")[0]+"~"+periodMap.get(period_index)+"");
        }
        return inventoryMapInner;
    }
    @Override
    public HashMap<Long , List<InventoryDataForRIDTO> > getListOFInventorForRI() {
        HashMap<Long, String> periodMap = getApiResponse(periodURL);
        HashMap<Long, String> scopeMap = getApiResponse(categoryURL);
        HashMap<Long , List<InventoryDataForRIDTO>> invdata = new HashMap<Long , List<InventoryDataForRIDTO>>();
        List<InventoryDataForRIDTO> inventoryDataList = new ArrayList<InventoryDataForRIDTO>();
        InventoryDataForRIDTO inventoryData = new InventoryDataForRIDTO();
        List<Object[]> result = inventoryRepo.findByStatusIsNot("-1 || -2").orElseThrow(() -> new ResourceNotFoundException("Not found"));
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Not found");
        }
        for (Object[] row : result) {
            inventoryData.setId(((Number) row[0]).longValue()) ;
            Long activity_index_index = ((Number) row[1]).longValue();
            inventoryData.setName(NoValueFound(((String) row[2])) ? "N/A " : ((String) row[2]) );
            inventoryData.setTco2(NoValueFound(((String) row[3])) ? "0" : ((String) row[3]));
            inventoryData.setPeriodName(periodMap.get(((Number) row[5]).longValue()));
            inventoryData.setScope(scopeMap.get(((Number) row[4]).longValue()).split("~")[0]); ;
            inventoryDataList.add(inventoryData);
            invdata.put(activity_index_index,inventoryDataList);
        }
        return invdata;
    }
    static boolean NoValueFound(final String str) {
        return (str == null) || (str.length() <= 0);
    }
    @Transactional
    public void mentainInventoryAlterationHistory(InventoryModel inventoryModel,String toalEmission,String createdBy,EmissionAlterationHistoryModel emissionAlterationHistoryModel){

        InventoryAlterationHistoryModel inventoryAlterationHistoryModel = new InventoryAlterationHistoryModel();
        inventoryAlterationHistoryModel.setInventoryIndex(inventoryModel);
        inventoryAlterationHistoryModel.setTotalEmission(toalEmission);
        inventoryAlterationHistoryModel.setCreatedBy(createdBy);
        inventoryAlterationHistoryModel.setEmissionAlterationHistoryIndex(emissionAlterationHistoryModel);
        inventoryAlterationHistoryRepo.save(inventoryAlterationHistoryModel);

    }



    @Override
    @Transactional
    public void baseLineThisInventory(Long eaId) {
        String[] statuslist = {"1"};
        int i = 0;
        List<InventoryModel> inventoryModelList = inventoryRepo.findByActivityIndexAndStatusIn(eaId, statuslist).orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        String[] updateableInventoriesIndexes = inventoryModelList.stream()
                .map(inventoryModel -> String.valueOf(inventoryModel.getId()))
                .toArray(String[]::new);
        inventoryRepo.updateInvForBaselineActivate(updateableInventoriesIndexes);

    }

    @Override
    @Transactional
    public void removeBaselineForThisInventory(Long eaId) {
        String[] statuslist = {"1","-1"};
        List<InventoryModel> inventoryModels = inventoryRepo.findByActivityIndexAndStatusIn(eaId, statuslist).orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        String[] updateableInventoriesIndexes = inventoryModels.stream()
                .map(inventoryModel -> String.valueOf(inventoryModel.getId()))
                .toArray(String[]::new);
        inventoryAlterationHistoryRepo.deletewhereInvIndexIn(updateableInventoriesIndexes);
        inventoryRepo.updateInvForBaselineDeActivate(null,updateableInventoriesIndexes);
    }

}
