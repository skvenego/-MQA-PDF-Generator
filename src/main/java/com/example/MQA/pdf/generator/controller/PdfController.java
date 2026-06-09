package com.example.MQA.pdf.generator.controller;

import com.example.MQA.pdf.generator.model.PdfRequest;
import com.example.MQA.pdf.generator.service.PdfReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PdfController {

    @Autowired
    private PdfReportService pdfReportService;

    /**
     * Endpoint to check database status and application info.
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        boolean isDbConnected = pdfReportService.checkDatabaseConnection();
        
        status.put("status", "UP");
        status.put("appName", "MQA PDF Generator");
        status.put("database", "PostgreSQL");
        status.put("connected", isDbConnected);
        
        return ResponseEntity.ok(status);
    }

    /**
     * Endpoint to generate a PDF based on JSON request payload.
     */
    @PostMapping("/pdf/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestBody PdfRequest request) {
        try {
            byte[] pdfBytes = pdfReportService.generatePdf(request);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = request.getTitle() != null 
                    ? request.getTitle().toLowerCase().replaceAll("[^a-z0-9]", "_") + ".pdf"
                    : "mqa_document.pdf";
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating PDF: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Endpoint to download a pre-formatted sample PDF report.
     */
    @GetMapping("/pdf/sample")
    public ResponseEntity<byte[]> getSamplePdf() {
        try {
            PdfRequest request = PdfRequest.builder()
                    .clientName("JAYRAMBHAI J RABARI")
                    .address("PLOT NO. 1084, GIDC, ROAD NO. 4 & 6, SACHIN, SURAT, GUJARAT – 394230, INDIA")
                    .standard("ISO 9001:2015")
                    .systemName("Quality Management System")
                    .scope("TRANSPORT OF BUILDING MATERIALS")
                    .certificateNo("QMS/26MX894")
                    .initialDate("28 May 2026")
                    .firstAuditDate("27 May 2027")
                    .secondAuditDate("27 May 2028")
                    .recertificationDate("27 May 2029")
                    .watermarkText("MQA SECURE")
                    .build();

            byte[] pdfBytes = pdfReportService.generatePdf(request);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "mqa_registration_certificate.pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error generating sample PDF: " + e.getMessage()).getBytes());
        }
    }
}
