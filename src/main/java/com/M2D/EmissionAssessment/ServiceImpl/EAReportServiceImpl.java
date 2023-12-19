package com.M2D.EmissionAssessment.ServiceImpl;


import com.M2D.EmissionAssessment.DTO.General.CategoryByScopeDTO;
import com.M2D.EmissionAssessment.Exception.ResourceNotFoundException;
import com.M2D.EmissionAssessment.Model.EAModel;
import com.M2D.EmissionAssessment.MultiTenancy.RequestInterceptor;
import com.M2D.EmissionAssessment.Repository.EARepo;
import com.M2D.EmissionAssessment.Repository.InventoryRepo;
import com.M2D.EmissionAssessment.Services.EAReportService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

@Service
public class EAReportServiceImpl implements EAReportService {

    @Autowired
    private EARepo eaRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WebClient.Builder webclient;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Value("${categoryByScopeURL}")
    String categoryByScopeURL;

    @Value("${tagURL}")
    private String tagURL;

    @Value("${systemUserURL}")
    private String systemUserURL;

    @Value("${siteURL}")
    private String siteURL;

    @Value("${periodURL}")
    private String periodURL;

    @Value("${organizationURL}")
    private String organizationURL;

    @Value("${imagePath}")
    private String imagePath;



    @Value("${filePath}")
    private String filePath;

    @Override
    public ResponseEntity<byte[]> getPDFReport(Long eaId) throws IOException {
        String scope1Inventory = "";
        String scope2Inventory = "";
        String scope3Inventory = "";
        String assesserName = "";
        String siteName = "";
        String periodName = "";
        String organizationName = "";
        float title = 22;
        float h1 = 18.0f;
        float h2 = 16.0f;
        float h3 = 14.0f;
        float h4 = 12.0f;
        double scope1Total = 0.0;
        double scope2Total = 0.0;
        double scope3Total = 0.0;
        Integer totalEmission = 0;



        String imagePath1 = imagePath+"esgLogobg.jpg";


        EAModel eaModel = eaRepo.findById(eaId).orElseThrow(() -> new ResourceNotFoundException("Emission assessment not found"));
        totalEmission = inventoryRepo.getTotal(eaId).orElseThrow(() -> new ResourceNotFoundException("No record is available for this emission"));
        HashMap<Long, String> assesserNamesMap = getApiResponseHMP(systemUserURL);
        HashMap<Long, String> organizationMap = getApiResponseHMP(organizationURL);
        HashMap<Long, String> siteMap = getApiResponseHMP(siteURL);
        HashMap<Long, String> periodMap = getApiResponseHMP(periodURL);
        assesserName = (assesserNamesMap.get(eaModel.getAssesserIndex()) == null) ? "N/A" : assesserNamesMap.get(eaModel.getAssesserIndex())  ;
        organizationName = (organizationMap.get(eaModel.getOrganizationIndex()) == null) ? "N/A" : organizationMap.get(eaModel.getOrganizationIndex())  ;
        siteName = (siteMap.get(eaModel.getOrgSiteIndex()) == null) ? "N/A" : siteMap.get(eaModel.getOrgSiteIndex())  ;
        periodName = (periodMap.get(eaModel.getPeriodIndex()) == null) ? "N/A" : periodMap.get(eaModel.getPeriodIndex())  ;
        List<CategoryByScopeDTO> categoryByScopeDTOS1 = getApiResponse(categoryByScopeURL + "/Scope1");
        List<CategoryByScopeDTO> categoryByScopeDTOS2 = getApiResponse(categoryByScopeURL + "/Scope2");
        List<CategoryByScopeDTO> categoryByScopeDTOS3 = getApiResponse(categoryByScopeURL + "/Scope3");



        String outputFilePath = filePath+"output1.pdf";
        PdfWriter writer = new PdfWriter(outputFilePath);
        PdfDocument pdf = new PdfDocument(writer);
        pdf.setDefaultPageSize(PageSize.A2);
        Document document = new Document(pdf);
        document.setMargins(200.0F, 30.0F, 200.0F, 30.0F);

        EAReportServiceImpl.HeaderEventHandler headerHandler = new EAReportServiceImpl.HeaderEventHandler(imagePath);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);
        EAReportServiceImpl.FooterEventHandler footerHandler = new EAReportServiceImpl.FooterEventHandler(imagePath);
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);
//        Paragraph orgName = new Paragraph(organizationName);
        Paragraph orgName = new Paragraph();
        Text t =new Text(organizationName);
        t.setFontSize(title).setBold().setTextAlignment(TextAlignment.CENTER);
        orgName.add(t);
        document.add(orgName);

        Text t1 =new Text("Site: ").setFontSize(h1).setBold();
        Text t2 =new Text(siteName).setFontSize(h1);

        Paragraph division = new Paragraph( );
        division.add(t1);
        division.add(t2);
        document.add(division);

        Paragraph period = new Paragraph();
        t1.setText("Period: ");
        t2.setText(periodName);
        period.add(t1);
        period.add(t2);
        document.add(period);

        Paragraph total = new Paragraph();
        t1.setText("Toatal emission: ");
        t2.setText(""+totalEmission+"");
        total.add(t1);
        total.add(t2);
        total.setTextAlignment(TextAlignment.RIGHT);
        document.add(total);


        if (categoryByScopeDTOS1 != null) {

            for (CategoryByScopeDTO dto : categoryByScopeDTOS1) {
                if (dto.getId() != null) {
                    scope1Inventory += "" + dto.getId() + ",";
                }
            }
        }


        if (categoryByScopeDTOS2 != null) {
            for (CategoryByScopeDTO dto : categoryByScopeDTOS2) {
                if (dto.getId() != null) {
                    scope2Inventory += "" + dto.getId() + ",";
                }
            }
        }

        if (categoryByScopeDTOS3 != null) {
            for (CategoryByScopeDTO dto : categoryByScopeDTOS3) {
                if (dto.getId() != null) {
                    scope3Inventory += "" + dto.getId() + ",";
                }
            }
        }

        List<Object[]> queryResultForScope1 = eaRepo.findDataForEAReport(eaId, scope1Inventory).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        List<Object[]> queryResultForScope2 = eaRepo.findDataForEAReport(eaId, scope2Inventory).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        List<Object[]> queryResultForScope3 = eaRepo.findDataForEAReport(eaId, scope3Inventory).orElseThrow(() -> new ResourceNotFoundException("Not found"));

        Cell cell1 = new Cell();
        cell1.add(new Paragraph("Item Name").setFontSize(h2).setBold());
        cell1.setWidth(400f);

        Cell cell2 = new Cell();
        cell2.add(new Paragraph("Description").setFontSize(h2).setBold());
        cell2.setWidth(350f);

        Cell cell3 = new Cell();
        cell3.add(new Paragraph("Consumption").setFontSize(h2).setBold());
        cell3.setWidth(100f);

        Cell cell4 = new Cell();
        cell4.add(new Paragraph("Unit").setFontSize(h2).setBold());
        cell4.setWidth(100f);

        Cell cell5 = new Cell();
        cell5.add(new Paragraph("Factor").setFontSize(h2).setBold());
        cell5.setWidth(100f);

        Cell cell6 = new Cell();
        cell6.add(new Paragraph("Kg CO2 Emission").setFontSize(h2).setBold());
        cell6.setWidth(150f);


        if (!queryResultForScope1.isEmpty()) {

            Paragraph scope1 = new Paragraph("Emission scope 1");
            scope1.setBold();
            scope1.setFontSize(h1);
            document.add(scope1);

            Table table = new Table(6);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);

            for (Object[] row : queryResultForScope1) {

                table.addCell(NoValueFound(((String) row[0])) ? "N/A " : ((String) row[0]));//name
                table.addCell(NoValueFound(((String) row[1])) ? "N/A " : ((String) row[1]));//description
                table.addCell(NoValueFound(((String) row[2])) ? "N/A " : ((String) row[2]));// consumption
                table.addCell(NoValueFound(((String) row[3])) ? "N/A " : ((String) row[3]));// unit
                table.addCell(NoValueFound(((String) row[4])) ? "N/A " : ((String) row[4]));// factor
                table.addCell(NoValueFound(((String) row[5])) ? "N/A " : ((String) row[5]));// Kg Co2 emmission
                scope1Total += NoValueFound(((String) row[5])) ? 0 : Double.parseDouble((String) row[5]);

            }

            document.add(table);
            Paragraph totalScope1 = new Paragraph();
            t1.setText("Total Scope1: ");
            t2.setText(""+scope1Total+"");
            totalScope1.add(t1);
            totalScope1.add(t2);
            totalScope1.setTextAlignment(TextAlignment.RIGHT);
            document.add(totalScope1);
        }

        if (!queryResultForScope2.isEmpty()) {

            Paragraph scope2 = new Paragraph("Emission scope 2");
            scope2.setBold();
            scope2.setFontSize(h1);
            document.add(scope2);

            Table table2 = new Table(6);
            table2.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table2.addCell(cell1);
            table2.addCell(cell2);
            table2.addCell(cell3);
            table2.addCell(cell4);
            table2.addCell(cell5);
            table2.addCell(cell6);

            for (Object[] row : queryResultForScope2) {

                table2.addCell(NoValueFound(((String) row[0])) ? "N/A " : ((String) row[0]));//name
                table2.addCell(NoValueFound(((String) row[1])) ? "N/A " : ((String) row[1]));//description
                table2.addCell(NoValueFound(((String) row[2])) ? "N/A " : ((String) row[2]));// consumption
                table2.addCell(NoValueFound(((String) row[3])) ? "N/A " : ((String) row[3]));// unit
                table2.addCell(NoValueFound(((String) row[4])) ? "N/A " : ((String) row[4]));// factor
                table2.addCell(NoValueFound(((String) row[5])) ? "N/A " : ((String) row[5]));// Kg Co2 emmission
                System.out.println(row[5]);
                scope2Total += NoValueFound(((String) row[5])) ? 0 : Double.parseDouble((String) row[5]);

            }
            document.add(table2);
            Paragraph totalScope2 = new Paragraph();
            t1.setText("Total Scope2: ");
            t2.setText(""+scope2Total+"");
            totalScope2.add(t1);
            totalScope2.add(t2);
            totalScope2.setTextAlignment(TextAlignment.RIGHT);
            document.add(totalScope2);
        }

        if (!queryResultForScope3.isEmpty()) {
            Paragraph scope3 = new Paragraph("Emission scope 3");
            scope3.setBold();
            scope3.setFontSize(h1);
            document.add(scope3);

            Table table3 = new Table(6);
            table3.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table3.addCell(cell1);
            table3.addCell(cell2);
            table3.addCell(cell3);
            table3.addCell(cell4);
            table3.addCell(cell5);
            table3.addCell(cell6);

            for (Object[] row : queryResultForScope3) {

                table3.addCell(NoValueFound(((String) row[0])) ? "N/A " : ((String) row[0]));//name
                table3.addCell(NoValueFound(((String) row[1])) ? "N/A " : ((String) row[1]));//description
                table3.addCell(NoValueFound(((String) row[2])) ? "N/A " : ((String) row[2]));// consumption
                table3.addCell(NoValueFound(((String) row[3])) ? "N/A " : ((String) row[3]));// unit
                table3.addCell(NoValueFound(((String) row[4])) ? "N/A " : ((String) row[4]));// factor
                table3.addCell(NoValueFound(((String) row[5])) ? "N/A " : ((String) row[5]));// Kg Co2 emmission
                System.out.println(row[5]);
                scope3Total += NoValueFound(((String) row[5])) ? 0 : Double.parseDouble((String) row[5]);

            }
            document.add(table3);
            Paragraph totalScope3 = new Paragraph();
            t1.setText("Total Scope3: ");
            t2.setText(""+scope3Total+"");
            totalScope3.add(t1);
            totalScope3.add(t2);
            totalScope3.setTextAlignment(TextAlignment.RIGHT);
            document.add(totalScope3);

        }

        Paragraph totalEission = new Paragraph();
        t1.setText("Total: ");
        t2.setText(""+totalEmission+"");
        totalEission.add(t1);
        totalEission.add(t2);
        document.add(totalEission);

        Paragraph assesserParagraph = new Paragraph();
        t1.setText("Assesser: ");
        t2.setText(assesserName);
        assesserParagraph.add(t1);
        assesserParagraph.add(t2);
        document.add(assesserParagraph);
        document.close();

        File file = new File(outputFilePath);
        byte[] pdfData = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(pdfData);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("output1.pdf").build());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfData.length)
                .body(pdfData);


    }

    private byte[] generatePdfBytes() {
        return new byte[]{};
    }



    private static class HeaderEventHandler implements IEventHandler {
//        @Value("${imagePath}")
        private static String imagePath;

        public HeaderEventHandler(String imagePath) {
            this.imagePath = imagePath;
        }


        @SneakyThrows
        @Override
        public void handleEvent(com.itextpdf.kernel.events.Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfCanvas canvas = new PdfCanvas(docEvent.getPage());
            String imagePath1 = imagePath+"esgLogobg.jpg";
            ImageData imageData = ImageDataFactory.create(imagePath1);
            imageData.setWidth(350);
            imageData.setHeight(200);
            canvas.addImageAt(imageData, 440.0F, 1480.0F,  false);


        }
    }


    private static class FooterEventHandler implements IEventHandler {
        private static String imagePath;
        public FooterEventHandler(String imagePath) {
            this.imagePath = imagePath;
        }
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfCanvas canvas = new PdfCanvas(docEvent.getPage());
            String imagePath1 = imagePath+"letterhead_footer.jpg";
            ImageData imageData = null;

            try {
                imageData = ImageDataFactory.create(imagePath1);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            imageData.setWidth(1210.0F);
            imageData.setHeight(120F);
            canvas.addImageAt(imageData, 0.0F, 5.0F,  false);
        }


    }

    public List<CategoryByScopeDTO> getApiResponse(String URL) {
        try {
            List<CategoryByScopeDTO> container = webclient
                    .build()
                    .get()
                    .uri(URL)
                    .header(HttpHeaders.AUTHORIZATION, RequestInterceptor.Token,HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve().onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new ResourceNotFoundException("" + URL + " result is null")))
                    .bodyToMono(new ParameterizedTypeReference<List<CategoryByScopeDTO>>() {
                    }).block();
            return container;
        } catch (Exception e) {
            return null;
        }

    }


    static boolean NoValueFound(final String str) {
        return (str == null) || (str.length() <= 0);
    }

    public HashMap<Long, String> getApiResponseHMP(String URL) {

        try {
            HashMap<Long, String> container = new HashMap<>();
            container = webclient
                    .build()
                    .get()
                    .uri(URL)
                    .header(HttpHeaders.AUTHORIZATION, RequestInterceptor.Token,HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve().onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new ResourceNotFoundException("" + URL + " result is null")))
                    .bodyToMono(new ParameterizedTypeReference<HashMap<Long, String>>() {
                    }).block();

            return container;
        } catch (Exception e) {
            return null;
        }





    }

}
