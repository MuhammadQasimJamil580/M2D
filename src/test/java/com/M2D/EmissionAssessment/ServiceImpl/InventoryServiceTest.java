//package com.M2D.EmissionAssessment.ServiceImpl;
//
//
//import com.M2D.EmissionAssessment.DTO.Request.InventoryDTO_Req;
//import com.M2D.EmissionAssessment.DTO.Response.InventoryDTO_Resp;
//import com.M2D.EmissionAssessment.Exception.ResourceNotFoundException;
//import com.M2D.EmissionAssessment.Model.InventoryModel;
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
//public class InventoryServiceTest {
//    @Mock
//    private InventoryRepo inventoryRepo;
//
//    @Mock
//    private EARepo eaRepo;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @Mock
//    private WebClient.Builder webclient;
//
//
//    private String categoryURL = "http://localhost:6363/category";
//
//
//    private String subCategoryURL = "http://localhost:6363/subcategory";
//
//
//    private String standardURL = "http://localhost:6363/standard";
//
//
//    private String tagURL = "http://localhost:6363/tag";
//
//
//    private String gassURL = "http://localhost:6262/GHGGasses";
//
//    @InjectMocks
//    private InventoryServiceImpl inventoryService;
//
//    @BeforeEach
//    void setUp(){
//        this.inventoryService = new InventoryServiceImpl(this.inventoryRepo,this.eaRepo,this.modelMapper,this.webclient);
//    }
//    @Test
//    public void testGetAll_Success() {
//        Long eId = 1L;
//        List<InventoryModel> inventoryModels = new ArrayList<>();
//        InventoryModel inventoryModel = new InventoryModel();
//        inventoryModels.add(inventoryModel);
//        when(inventoryRepo.findByActivityIndexAndStatusIsNot(eId, "-1 || -2")).thenReturn(Optional.of(inventoryModels));
////        when(modelMapper.map(any(InventoryModel.class), eq(InventoryDTO_Resp.class))).thenReturn(new InventoryDTO_Resp());
//        List<InventoryDTO_Resp> result = inventoryService.getAll(eId);
//        assertNotNull(result);
//        assertEquals(1, result.size());
////        verify(inventoryRepo, times(1)).findByActivityIndexAndStatusIsNot(eId, "-1");
////        verify(modelMapper, times(1)).map(any(InventoryModel.class), eq(InventoryDTO_Resp.class));
////        verifyNoMoreInteractions(inventoryRepo, modelMapper);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void testGetAll_ResourceNotFound() {
//        Long eId = 1L;
//        when(inventoryRepo.findByActivityIndexAndStatusIsNot(eId, "-1 || -2")).thenReturn(Optional.empty());
//        inventoryService.getAll(eId);
//        verify(inventoryRepo, times(1)).findByActivityIndexAndStatusIsNot(eId, "-1 || -2");
//        verifyNoMoreInteractions(inventoryRepo, modelMapper);
//    }
//
//    @Test
//    public void testGetOneRecord_Success() {
//        Long invId = 1L;
//        InventoryModel inventoryModel = new InventoryModel();
//        inventoryModel.setId(invId);
////        when(inventoryRepo.findById(invId)).thenReturn(Optional.of(inventoryModel));
////        when(modelMapper.map(any(InventoryModel.class), eq(InventoryDTO_Req.class))).thenReturn(new InventoryDTO_Req());
////        InventoryDTO_Req result = inventoryService.getOneRecord(invId);
//        assertNotNull(inventoryModel);
//        assertEquals(invId, inventoryModel.getId());
////        verify(inventoryRepo, times(1)).findById(invId);
////        verify(modelMapper, times(1)).map(any(InventoryModel.class), eq(InventoryDTO_Req.class));
////        verifyNoMoreInteractions(inventoryRepo, modelMapper);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void testGetOneRecord_ResourceNotFound() {
//        Long invId = 1L;
//        when(inventoryRepo.findById(invId)).thenReturn(Optional.empty());
//        inventoryService.getOneRecord(invId);
//        verify(inventoryRepo, times(1)).findById(invId);
//        verifyNoMoreInteractions(inventoryRepo, modelMapper);
//    }
//
//    @Test
//    public void testSaveInventory_Success() {
////        InventoryDTO_Req inventoryDTO_req = new InventoryDTO_Req();
//        InventoryModel inventoryModel = new InventoryModel();
//        inventoryModel.setName("test1");
//        inventoryModel.setId(1L);
//        inventoryRepo.save(inventoryModel);
////        when(inventoryRepo.findById(1L)).thenReturn(Optional.of(inventoryModel));
//        assertEquals("test1", inventoryModel.getName());
////        when(modelMapper.map(inventoryDTO_req, InventoryModel.class)).thenReturn(inventoryModel);
////        when(modelMapper.map(any(), eq(EAModel.class))).thenReturn(new EAModel());
////        inventoryService.saveInventory(inventoryDTO_req);
////        verify(modelMapper, times(1)).map(inventoryDTO_req, InventoryModel.class);
////        verify(modelMapper, times(1)).map(any(), eq(EAModel.class));
////        verify(inventoryRepo, times(1)).save(inventoryModel);
////        verifyNoMoreInteractions(inventoryRepo, modelMapper);
//    }
//
//    @Test
//    public void testUpdateInventory_Success() {
//        Long invId = 1L;
////        InventoryDTO_Req inventoryDTO_req = new InventoryDTO_Req();
//        InventoryModel inventoryModel = new InventoryModel();
//        inventoryModel.setName("test1");
//        inventoryModel.setId(invId);
//        inventoryRepo.save(inventoryModel);
//        inventoryModel.setName("test2");
//        inventoryModel.setId(2L);
//        inventoryRepo.save(inventoryModel);
//
//
////        when(inventoryRepo.findById(2L)).thenReturn(Optional.of(inventoryModel));
//
//        assertEquals("test2", inventoryModel.getName());
//        assertEquals(2L, inventoryModel.getId());
//
//        inventoryModel.setName("test2Update");
//        inventoryModel.setId(2L);
//        inventoryRepo.save(inventoryModel);
//
//        assertEquals("test2Update", inventoryModel.getName());
//        assertEquals(2L, inventoryModel.getId());
//
//
////        when(modelMapper.map(inventoryDTO_req, inventoryModel)).thenReturn(inventoryModel);
////        when(modelMapper.map(any(), eq(EAModel.class))).thenReturn(new EAModel());
////        inventoryService.updateInventory(inventoryDTO_req, invId);
////        verify(inventoryRepo, times(1)).findById(invId);
////        verify(modelMapper, times(1)).map(inventoryDTO_req, inventoryModel);
////        verify(modelMapper, times(1)).map(any(), eq(EAModel.class));
////        verify(inventoryRepo, times(1)).save(inventoryModel);
////        verifyNoMoreInteractions(inventoryRepo, modelMapper);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void testUpdateInventory_ResourceNotFound() {
//        Long invId = 1L;
//        InventoryDTO_Req inventoryDTO_req = new InventoryDTO_Req();
//        when(inventoryRepo.findById(invId)).thenReturn(Optional.empty());
//        inventoryService.updateInventory(inventoryDTO_req, invId);
//        verify(inventoryRepo, times(1)).findById(invId);
//        verifyNoMoreInteractions(inventoryRepo, modelMapper);
//    }
//
//    @Test
//    public void testDeleteInventory_Success() {
//        Long invId = 1L;
//        InventoryModel inventoryModel = new InventoryModel();
//        when(inventoryRepo.findById(invId)).thenReturn(Optional.of(inventoryModel));
//        inventoryService.deleteInventory(invId);
//        verify(inventoryRepo, times(1)).findById(invId);
//        verify(inventoryRepo, times(1)).save(inventoryModel);
//        verifyNoMoreInteractions(inventoryRepo);
//    }
//
//    @Test(expected = ResourceNotFoundException.class)
//    public void testDeleteInventory_ResourceNotFound() {
//        Long invId = 1L;
//        when(inventoryRepo.findById(invId)).thenReturn(Optional.empty());
//        inventoryService.deleteInventory(invId);
//        verify(inventoryRepo, times(1)).findById(invId);
//        verifyNoMoreInteractions(inventoryRepo);
//    }
//
//}
