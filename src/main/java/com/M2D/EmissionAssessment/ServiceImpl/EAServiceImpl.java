package com.M2D.EmissionAssessment.ServiceImpl;

import com.M2D.EmissionAssessment.DTO.Request.EADTO_Req;
import com.M2D.EmissionAssessment.DTO.Response.EADTO_Resp;
import com.M2D.EmissionAssessment.Exception.ResourceNotFoundException;
import com.M2D.EmissionAssessment.Model.EAModel;
import com.M2D.EmissionAssessment.Model.EmissionAlterationHistoryModel;
import com.M2D.EmissionAssessment.MultiTenancy.RequestInterceptor;
import com.M2D.EmissionAssessment.Repository.*;
import com.M2D.EmissionAssessment.Services.EAService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.hibernate.internal.CoreLogging.logger;

@Service
@Slf4j

//@RequiredArgsConstructor
public class EAServiceImpl implements EAService {

    //    final EARepo eaRepo;
//    final ModelMapper modelMapper;
//    final InventoryRepo inventoryRepo;
//    final WebClient.Builder webclient;
    @Autowired
    private EARepo eaRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private WebClient.Builder webclient;

    @Value("${siteURL}")
    private String siteURL;
    @Value("${periodURL}")
    private String periodURL;
    @Value("${systemUserURL}")
    private String systemUserURL;
    @Autowired
    private EmissionAlterationHistoryRepo emissionHistoryRepo;

    Logger logger = LoggerFactory.getLogger(EAServiceImpl.class);

    @Override
    public List<EADTO_Resp> getAll() {
        List<String> statuslist = Arrays.asList("1");

        ExecutorService executor = Executors.newFixedThreadPool(5);
        Future<HashMap<Long, String>> periodFuture = executor.submit(() -> getApiResponse(periodURL));
        Future<HashMap<Long, String>> sitFuture = executor.submit(() -> getApiResponse(siteURL));
        Future<HashMap<Long, String>> assesserFuture = executor.submit(() -> getApiResponse(systemUserURL));
        Future<HashMap<Long, Integer>> inventoryFuture = executor.submit(() -> getCountOfInventory());
        Future<HashMap<Long, Integer>> totalEmissionFuture = executor.submit(() -> getTotalEmission());

        List<EAModel> eaModel = eaRepo.findByStatusIn(statuslist).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        if (eaModel.isEmpty()) {
            throw new ResourceNotFoundException("Emission Assessment not found");
        }

        List<EADTO_Resp> eadto_resps = eaModel.stream().map(eaModels -> {
            try {
                HashMap<Long, String>  periodMap =   periodFuture.get();
                HashMap<Long, String>  sitMap =   sitFuture.get();
                HashMap<Long, String>  assesserMap =   assesserFuture.get();
                HashMap<Long, Integer> inventoryCount = inventoryFuture.get();
                HashMap<Long, Integer> totalEmission =  totalEmissionFuture.get();
                executor.shutdown();
                return this.mapEADto(eaModels, sitMap, periodMap, assesserMap, inventoryCount, totalEmission);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return eadto_resps;
    }

    @Override
    public List<EADTO_Req> getAllEADropdown() {
        List<String> statuslist = Arrays.asList("1");
        List<EAModel> eaModel = eaRepo.findByStatusIn(statuslist).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        if (eaModel.isEmpty()) {
            throw new ResourceNotFoundException("Emission Assessment not found");
        }
        List<EADTO_Req> eadto_reqs = eaModel.stream().map(x -> modelMapper.map(x, EADTO_Req.class)).collect(Collectors.toList());
        return eadto_reqs;
    }

    @Override
    public List<EADTO_Req> getAllBaselineEADropdown() {
        List<String> statuslist = Arrays.asList("1");
        List<EAModel> eaModel = eaRepo.getDataForBaseline().orElseThrow(() -> new ResourceNotFoundException("Not found"));
        if (eaModel.isEmpty()) {
            throw new ResourceNotFoundException("Emission Assessment not found");
        }
        List<EADTO_Req> eadto_reqs = eaModel.stream().map(x -> modelMapper.map(x, EADTO_Req.class)).collect(Collectors.toList());
        return eadto_reqs;
    }

    @Override
    public EADTO_Req getOneRecord(Long eaId) {
        EAModel eaModel = this.eaRepo.findById(eaId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        EADTO_Req eadto_req = modelMapper.map(eaModel, EADTO_Req.class);
        return eadto_req;
    }

    @Override
    public void saveEA(EADTO_Req eadto_req) {
        EAModel eaModel = modelMapper.map(eadto_req, EAModel.class);
        eaModel.setId(null);
        eaRepo.save(eaModel);
    }

    @Override
    public void updateEA(EADTO_Req eadto_req, Long eaId) {
        EAModel eaModel = eaRepo.findById(eaId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        modelMapper.map(eadto_req, eaModel);
        eaModel.setId(eaId);
        eaRepo.save(eaModel);
    }

    @Override
    public void deactivateEA(Long eaId) {
        EAModel eaModel = eaRepo.findById(eaId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        if (eaModel.getStatus().equals("0")) {
            throw new ResourceNotFoundException("already inactive");
        } else {
            eaModel.setStatus("0");
            eaRepo.save(eaModel);
        }
    }

    @Override
    public void activateEA(Long eaId) {
        EAModel eaModel = eaRepo.findById(eaId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        if (eaModel.getStatus().equals("1")) {
            throw new ResourceNotFoundException("already active");
        } else {
            eaModel.setStatus("1");
            eaRepo.save(eaModel);
        }
    }

    @Override
    @Transactional
    public void baseLineThisEA(Long eaId) {
        Double totalEmission = inventoryRepo.totalEmissionByEID(eaId);
        EAModel eaModel = eaRepo.findById(eaId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        eaModel.setBaselineTotalEmission(String.valueOf(totalEmission));
        eaModel.setId(eaId);
        eaRepo.save(eaModel);
        mentainEmissionAlterationHistory( eaModel,  String.valueOf(totalEmission), String.valueOf(RequestInterceptor.UserId) );
    }

    @Override
    @Transactional
    public void removeBaselineForThisEA(Long eaId) {
        EAModel eaModel = eaRepo.findById(eaId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        eaModel.setBaselineTotalEmission(null);
        emissionHistoryRepo.deleteByEmissionIndex(eaModel.getId());
        eaRepo.save(eaModel);
    }


    public EADTO_Resp mapEADto(EAModel eaModel, HashMap<Long, String> siteMap, HashMap<Long, String> periodMap, HashMap<Long, String> assesserMap, HashMap<Long, Integer> inventoryCount, HashMap<Long, Integer> totalEmission) throws IOException {

        EADTO_Resp eadto_resp = new EADTO_Resp();
        modelMapper.map(eaModel, eadto_resp);

        if (eaModel.getOrgSiteIndex() != null && siteMap != null) {
            if (siteMap.get(eaModel.getOrgSiteIndex()) == null) {
                eadto_resp.setSiteName("N/A");
            } else {
                eadto_resp.setSiteName(siteMap.get(eaModel.getOrgSiteIndex()));
            }
        } else {
            eadto_resp.setSiteName("N/A");
        }

        if (eaModel.getPeriodIndex() != null && periodMap != null) {
            if (periodMap.get(eaModel.getPeriodIndex()) == null) {
                eadto_resp.setPeriodName("N/A");
            } else {
                eadto_resp.setPeriodName(periodMap.get(eaModel.getPeriodIndex()));
            }
        } else {
            eadto_resp.setPeriodName("N/A");
        }

        if (eaModel.getAssesserIndex() != null && assesserMap != null) {
            if (assesserMap.get(eaModel.getAssesserIndex()) == null) {
                eadto_resp.setAssesserName("N/A");
            } else {
                eadto_resp.setAssesserName(assesserMap.get(eaModel.getAssesserIndex()));
            }
        } else {
            eadto_resp.setAssesserName("N/A");
        }

        if (eaModel.getId() != null && inventoryCount != null && totalEmission != null) {

            if (inventoryCount.get(eaModel.getId()) != null) {
                eadto_resp.setInventoryCount(inventoryCount.get(eaModel.getId()));
            } else {
                eadto_resp.setInventoryCount(0);
            }

            if (totalEmission.get(eaModel.getId()) != null) {
                eadto_resp.setTotalEmission(totalEmission.get(eaModel.getId()));
            } else {
                eadto_resp.setTotalEmission(0);
            }

        } else {
            eadto_resp.setInventoryCount(0);
            eadto_resp.setTotalEmission(0);
        }

        return eadto_resp;
    }

    public HashMap<Long, String> getApiResponse(String URL) {
        try {
            HashMap<Long, String> container = new HashMap<>();
            container = webclient
                    .build()
                    .get()
                    .uri(URL)
                    .header(HttpHeaders.AUTHORIZATION, RequestInterceptor.Token  ,HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<HashMap<Long, String>>() {
                    }).block();
            if(container == null){
             throw new ResourceNotFoundException("Service is down or there is no data in that service");
            }else{
                return container;
            }
        } catch (Exception e) {
            return null;
        }
    }


    public HashMap<Long, Integer> getCountOfInventory() {
        HashMap<Long, Integer> countmap = new HashMap<Long, Integer>();
        List<Object[]> result = inventoryRepo.countByQuery();
        for (Object[] row : result) {
            Long activityIndex = ((Number) row[0]).longValue();
            Integer count = ((Number) row[1]).intValue();
            countmap.put(activityIndex, count);
        }
        return countmap;
    }

    public HashMap<Long, Integer> getTotalEmission() {

        HashMap<Long, Integer> totalEmission = new HashMap<Long, Integer>();
        List<Object[]> result = inventoryRepo.totalByEmissionIdForAllEA();
        for (Object[] row : result) {
            Long activityIndex = ((Number) row[0]).longValue();
            Integer total = ((Number) row[1]).intValue();
            totalEmission.put(activityIndex, total);

        }
        return totalEmission;
    }

    public EAServiceImpl(EARepo eaRepo, ModelMapper modelMapper, InventoryRepo inventoryRepo, WebClient.Builder webclient) {
        this.eaRepo = eaRepo;
        this.modelMapper = modelMapper;
        this.inventoryRepo = inventoryRepo;
        this.webclient = webclient;
    }
@Transactional
   public EmissionAlterationHistoryModel mentainEmissionAlterationHistory(EAModel eaModel, String toalEmission, String createdBy){

        EmissionAlterationHistoryModel emissionAlterationHistoryModel = new EmissionAlterationHistoryModel();
        emissionAlterationHistoryModel.setEmissionIndex(eaModel);
        emissionAlterationHistoryModel.setTotalEmission(toalEmission);
        emissionAlterationHistoryModel.setCreatedBy(createdBy);
        emissionHistoryRepo.save(emissionAlterationHistoryModel);
        return emissionAlterationHistoryModel;


    }
}
