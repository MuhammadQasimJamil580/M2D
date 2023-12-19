//package com.M2D.EmissionAssessment.Controller;
//
//import com.M2D.EmissionAssessment.DTO.Request.EADTO_Req;
//import com.M2D.EmissionAssessment.DTO.Response.EADTO_Resp;
//import com.M2D.EmissionAssessment.Exception.ApiResponse;
//import com.M2D.EmissionAssessment.Services.EAService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)
//class EAControllerTest {
//
//    @Mock
//    private EAService eaService;
//
////    private MockMvc mockMvc;
//    @InjectMocks
//    private EAController eaController;
//
//    @BeforeEach
//    void setUp() {
////        this.eaController = new EAController(this.eaService);
////        mockMvc = MockMvcBuilders.standaloneSetup(EAControllerTest.class).build();
//    }
//
//
//
//    @Test
//    void testGetAll() {
//        // Initialize the mocks
//        MockitoAnnotations.initMocks(this);
//
//        // Create sample data for the test
//        List<EADTO_Resp> mockResponse = new ArrayList<>();
//        // Add sample EADTO_Resp objects to the mockResponse list
//
//        // Call the method under test
////        ResponseEntity<List<EADTO_Resp>> responseEntity = EAController.getAll();
//
//        // Verify the result
////        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
////        assertEquals(mockResponse, responseEntity.getBody());
//    }
//
//@Test
//    public void testGetById() throws Exception {
//
//     Long id = 1L;
//    MockitoAnnotations.initMocks(this);
//   EADTO_Resp mockResponse = new EADTO_Resp();
////    ResponseEntity<EADTO_Req> responseEntity = EAController.getById(id);
////    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//    }
//
//    @Test
//    public void testSaveEA() {
//        // Arrange
//        EADTO_Req eadto_req = new EADTO_Req(1L,"name","description",1L,1L,1,1L, null, null,1L);
//        ResponseEntity<ApiResponse> response = eaController.saveEA(eadto_req);
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals("Saved successfully", response.getBody().getMessage());
//        assertEquals(true, response.getBody().isSuccess());
//        verify(eaService).saveEA(eadto_req);
//    }
//
//    @Test
//    public void testUpdateEA() {
//        // Mock input data
//        EADTO_Req eadtoReq = new EADTO_Req();
//        Long id = 123L;
//        // Mock the behavior of the eaService
//        doNothing().when(eaService).updateEA(eadtoReq, id);
//        // Call the controller method
//        ResponseEntity<ApiResponse> response = eaController.updateEA(eadtoReq, id);
//        // Verify the interactions and assertions
//        verify(eaService, times(1)).updateEA(eadtoReq, id);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Updated successfully", response.getBody().getMessage());
//        assertTrue(response.getBody().isSuccess());
//    }
//
//    @Test
//    public void testDeactivateEA() {
//
//        Long id = 123L;
//        String expectedMessage = "deactivated";
//        boolean expectedStatus = true;
//        ResponseEntity<ApiResponse> response = eaController.deleteEA(id);
//        Mockito.verify(eaService).deactivateEA(id);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedMessage, response.getBody().getMessage());
//        assertEquals(expectedStatus, response.getBody().isSuccess());
//    }
//
//    @Test
//    public void testActivateEA_SuccessfulActivation() {
//        // Arrange
//        Long id = 1L;
//        doNothing().when(eaService).activateEA(id);
//        // Act
//        ResponseEntity<ApiResponse> response = eaController.activateEA(id);
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("activated", response.getBody().getMessage());
//        assertTrue(response.getBody().isSuccess());
//        // Verify that the service method was called
//        verify(eaService).activateEA(id);
//    }
//
//}