//package com.M2D.EmissionAssessment.Controller;
//
//import com.M2D.EmissionAssessment.DTO.Request.InventoryDTO_Req;
//import com.M2D.EmissionAssessment.DTO.Response.InventoryDTO_Resp;
//import com.M2D.EmissionAssessment.Exception.ApiResponse;
//import com.M2D.EmissionAssessment.Services.InventoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)
//class InventoryControllerTest {
//    @Mock
//    private InventoryService inventoryService;
//
//
//    @InjectMocks
//    private InventoryController inventoryController;
//
//    @BeforeEach
//    void setUp() {
//        this.inventoryController = new InventoryController(this.inventoryService);
//    }
//
//    @Test
//    public void testGetAll() {
//        // Arrange
//        Long eId = 123L;
//        List<InventoryDTO_Resp> mockInventoryDTOs = Arrays.asList(
//                new InventoryDTO_Resp(),
//                new InventoryDTO_Resp()
//        );
//        when(inventoryService.getAll(eId)).thenReturn(mockInventoryDTOs);
//        // Act
//        ResponseEntity<List<InventoryDTO_Resp>> responseEntity = inventoryController.getAll(eId);
//        // Assert
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isEqualTo(mockInventoryDTOs);
//        verify(inventoryService).getAll(eId);
//    }
//    @Test
//    public void testGetOneRecord() throws Exception {
//        Long id = 1L;
//        MockitoAnnotations.initMocks(this);
//        InventoryDTO_Req inventoryDTO_req = new InventoryDTO_Req();
//        ResponseEntity<InventoryDTO_Req> responseEntity = inventoryController.getOneRecord(id);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//    }
//
//
//    @Test
//    public void testSaveInventory() throws Exception{
//        // Arrange
//        InventoryDTO_Req inventoryDTO_req = new InventoryDTO_Req();
//        ResponseEntity<ApiResponse> response = inventoryController.saveInventory(inventoryDTO_req);
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals("created successfully", response.getBody().getMessage());
//        assertEquals(true, response.getBody().isSuccess());
//        verify(inventoryService).saveInventory(inventoryDTO_req);
//    }
//
//    @Test
//    public void testUpdateInventory() {
//        // Create mock dependencies
////        InventoryService inventoryService = mock(InventoryService.class);
//
//        // Create the instance of your controller class
////        InventoryController controller = new YourControllerClass(inventoryService);
//
//        // Prepare test data
//        InventoryDTO_Req inventoryDTO_req = new InventoryDTO_Req();
//        Long id = 123L;
//
//        // Perform the test
//        ResponseEntity<ApiResponse> response = inventoryController.updateInventory(inventoryDTO_req, id);
//
//        // Verify the interactions with the mock dependencies
//        verify(inventoryService).updateInventory(inventoryDTO_req, id);
//
//        // Verify the response
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("updated successfully", response.getBody().getMessage());
//        assertTrue(response.getBody().isSuccess());
//    }
//
//
//    @Test
//    void deleteInventory_ShouldReturnSuccessResponse() {
//        // Arrange
//        Long id = 1L;
//        // Act
//        ResponseEntity<ApiResponse> response = inventoryController.deleteInventory(id);
//        // Assert
//        verify(inventoryService, times(1)).deleteInventory(id);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("deleted successfully", response.getBody().getMessage());
//        assertTrue(response.getBody().isSuccess());
//    }
//
//
//
//
//}