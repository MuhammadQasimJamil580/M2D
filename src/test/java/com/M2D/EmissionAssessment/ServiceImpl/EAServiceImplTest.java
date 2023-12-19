//package com.M2D.EmissionAssessment.ServiceImpl;
//
//import com.M2D.EmissionAssessment.DTO.Request.EADTO_Req;
//import com.M2D.EmissionAssessment.DTO.Response.EADTO_Resp;
//import com.M2D.EmissionAssessment.Exception.ResourceNotFoundException;
//import com.M2D.EmissionAssessment.Model.EAModel;
//import com.M2D.EmissionAssessment.Repository.EARepo;
//import com.M2D.EmissionAssessment.Repository.InventoryRepo;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)
//public class EAServiceImplTest {
//
//    @Mock
//    private EARepo eaRepo;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @Mock
//    private InventoryRepo inventoryRepo;
//
//    @Mock
//    private WebClient.Builder webclient;
//
//
//
////    @Value("${siteURL}")
//    private String siteURL = "http://localhost:6363/site";
//
////    @Value("${periodURL}")
//    private String periodURL = "http://localhost:6363/period";
//
////    @Value("${systemUserURL}")
//    private String systemUserURL = "http://localhost:6262/user";
//
//    @InjectMocks
//    private EAServiceImpl eaService;
//
//    @BeforeEach
//    void setUp(){
////        this.eaService = new EAServiceImpl(this.eaRepo,this.modelMapper,this.inventoryRepo,this.webclient);
//    }
//
//
//    @Test
//    public void testGetOneRecord_Success() {
//
//        Long eaId = 1L;
//        EAModel eaModel = new EAModel();
//        eaModel.setId(eaId);
//        eaModel.setStatus("1");
//        eaRepo.save(eaModel);
////        when(eaRepo.findById(eaId)).thenReturn(Optional.of(eaModel));
//        assertNotNull(eaModel);
//        assertEquals(eaId, eaModel.getId());
////        verify(eaRepo, times(1)).findById(eaId);
////        verifyNoMoreInteractions(eaRepo);
//
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void testGetOneRecord_ResourceNotFound() {
//        Long eaId = 1L;
//        when(eaRepo.findById(eaId)).thenReturn(Optional.empty());
//        eaService.getOneRecord(eaId);
////        verify(eaRepo, times(1)).findById(eaId);
////        verifyNoMoreInteractions(eaRepo);
//    }
//
//    @Test
//    public void testSaveEA_Success() {
//        EADTO_Req request = new EADTO_Req();
//        EAModel eaModel = new EAModel();
//        when(modelMapper.map(request, EAModel.class)).thenReturn(eaModel);
//        eaService.saveEA(request);
//        verify(modelMapper, times(1)).map(request, EAModel.class);
//        verify(eaRepo, times(1)).save(eaModel);
//        verifyNoMoreInteractions(modelMapper, eaRepo);
//    }
//
//    @Test
//    public void testUpdateEA_Success() {
//        Long eaId = 1L;
//        EADTO_Req request = new EADTO_Req();
//        EAModel eaModel = new EAModel();
//        when(eaRepo.findById(eaId)).thenReturn(Optional.of(eaModel));
//        eaService.updateEA(request, eaId);
//        verify(eaRepo, times(1)).findById(eaId);
//        verify(modelMapper, times(1)).map(request, eaModel);
//        verify(eaRepo, times(1)).save(eaModel);
//        verifyNoMoreInteractions(eaRepo, modelMapper);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void testUpdateEA_ResourceNotFound() {
//        Long eaId = 1L;
//        EADTO_Req request = new EADTO_Req();
//        when(eaRepo.findById(eaId)).thenReturn(Optional.empty());
//        eaService.updateEA(request, eaId);
//        verify(eaRepo, times(1)).findById(eaId);
//        verifyNoMoreInteractions(eaRepo, modelMapper);
//    }
//
//    @Test
//    public void testDeactivateEA_Success() {
//        Long eaId = 1L;
//        EAModel eaModel = new EAModel();
//        eaModel.setId(eaId);
//        eaModel.setStatus("1");
//        eaRepo.save(eaModel);
//        when(eaRepo.findById(eaId)).thenReturn(Optional.of(eaModel));
//        eaService.deactivateEA(eaId);
////        when(eaRepo.findById(eaId)).thenReturn(Optional.of(eaModel));
//        assertEquals("0", eaModel.getStatus());
////        verify(eaRepo, times(1)).findById(eaId);
////        verify(eaRepo, times(1)).save(eaModel);
////        verifyNoMoreInteractions(eaRepo);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void testDeactivateEA_AlreadyInactive() {
//        Long eaId = 1L;
//        EAModel eaModel = new EAModel();
//        eaModel.setStatus("0");
//        when(eaRepo.findById(eaId)).thenReturn(Optional.of(eaModel));
//        eaService.deactivateEA(eaId);
//        verify(eaRepo, times(1)).findById(eaId);
//        verifyNoMoreInteractions(eaRepo);
//    }
//
//    @Test
//    public void testActivateEA_Success() {
//        Long eaId = 1L;
//        EAModel eaModel = new EAModel();
//        eaModel.setId(eaId);
//        eaModel.setStatus("0");
//        eaRepo.save(eaModel);
//        when(eaRepo.findById(eaId)).thenReturn(Optional.of(eaModel));
//        eaService.activateEA(eaId);
////        when(eaRepo.findById(eaId)).thenReturn(Optional.of(eaModel));
//        assertEquals("1", eaModel.getStatus());
////        verify(eaRepo, times(1)).findById(eaId);
////        verify(eaRepo, times(1)).save(eaModel);
////        verifyNoMoreInteractions(eaRepo);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void testActivateEA_AlreadyActive() {
//        Long eaId = 1L;
//        EAModel eaModel = new EAModel();
//        eaModel.setStatus("1");
//        when(eaRepo.findById(eaId)).thenReturn(Optional.of(eaModel));
//        eaService.activateEA(eaId);
//        verify(eaRepo, times(1)).findById(eaId);
//        verifyNoMoreInteractions(eaRepo);
//    }
//
//    @Test
//    public void testGetAll_Success() throws IOException {
//        List<EAModel> eaModels = new ArrayList<>();
//        EAModel eaModel = new EAModel();
//        eaModel.setId(1L);
//        eaModels.add(eaModel);
//        when(eaRepo.findByStatusIsNot("-1")).thenReturn(Optional.of(eaModels));
//        //temporary
////        when(eaService.getCountOfInventory(eq(1L))).thenReturn(1);
////        when(eaService.getTotalEmission(eq(1L))).thenReturn(100);
//        List<EADTO_Resp> result = eaService.getAll();
//        assertNotNull(result);
//        assertEquals(1, result.size());
////        verify(eaRepo, times(1)).findByStatusIsNot("-1");
////        verify(eaRepo, times(1)).findById(1L);
////        verify(eaService, times(1)).getCountOfInventory(eq(1L));
////        verify(eaService, times(1)).getTotalEmission(eq(1L));
////        verifyNoMoreInteractions(eaRepo, eaService);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void testGetAll_ResourceNotFound() {
//        when(eaRepo.findByStatusIsNot("-1")).thenReturn(Optional.empty());
//        eaService.getAll();
//        verify(eaRepo, times(1)).findByStatusIsNot("-1");
//        verifyNoMoreInteractions(eaRepo);
//    }
//
//    // Additional test cases can be added for the remaining methods and edge cases
//}
