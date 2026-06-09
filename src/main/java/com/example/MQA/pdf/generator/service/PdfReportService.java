package com.example.MQA.pdf.generator.service;

import com.example.MQA.pdf.generator.model.PdfRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

@Service
public class PdfReportService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${pdf.watermark.text}")
    private String defaultWatermark;

    private String cssContent;
    private String logoGlobeBase64;
    private String logoTextBase64;
    private String sealMqaBase64;
    private String sealUkafBase64;
    private String sealGoldBase64;

    @PostConstruct
    public void init() {
        try {
            // Load and cache PDF CSS
            ClassPathResource cssResource = new ClassPathResource("static/css/pdf-style.css");
            cssContent = StreamUtils.copyToString(cssResource.getInputStream(), StandardCharsets.UTF_8);

            // Load and cache Globe Logo PNG, converting it to Base64 for inline embedding
            ClassPathResource globeResource = new ClassPathResource("static/images/logo_globe.png");
            byte[] globeBytes = StreamUtils.copyToByteArray(globeResource.getInputStream());
            logoGlobeBase64 = Base64.getEncoder().encodeToString(globeBytes);

            // Load and cache Text Logo PNG
            ClassPathResource textResource = new ClassPathResource("static/images/logo_text.png");
            byte[] textBytes = StreamUtils.copyToByteArray(textResource.getInputStream());
            logoTextBase64 = Base64.getEncoder().encodeToString(textBytes);

            // Load and cache Seal MQA PNG
            ClassPathResource sealMqaResource = new ClassPathResource("static/images/seal_mqa.png");
            byte[] sealMqaBytes = StreamUtils.copyToByteArray(sealMqaResource.getInputStream());
            sealMqaBase64 = Base64.getEncoder().encodeToString(sealMqaBytes);

            // Load and cache Seal UKAF PNG
            ClassPathResource sealUkafResource = new ClassPathResource("static/images/seal_ukaf.png");
            byte[] sealUkafBytes = StreamUtils.copyToByteArray(sealUkafResource.getInputStream());
            sealUkafBase64 = Base64.getEncoder().encodeToString(sealUkafBytes);

            // Load and cache Seal Gold PNG
            ClassPathResource sealGoldResource = new ClassPathResource("static/images/seal_gold_blank.png");
            byte[] sealGoldBytes = StreamUtils.copyToByteArray(sealGoldResource.getInputStream());
            sealGoldBase64 = Base64.getEncoder().encodeToString(sealGoldBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize PDF static assets", e);
        }
    }

    /**
     * Checks if the application is connected to the PostgreSQL database.
     */
    public boolean checkDatabaseConnection() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Generates a PDF byte array using Thymeleaf and Flying Saucer ITextRenderer.
     */
    public byte[] generatePdf(PdfRequest request) throws Exception {
        Context context = new Context();
        
        // Populate dynamic variables
        context.setVariable("title", request.getTitle() != null ? request.getTitle() : "MQA PDF Report");
        context.setVariable("subtitle", request.getSubtitle() != null ? request.getSubtitle() : "Generated Report");
        context.setVariable("content", request.getContent() != null ? request.getContent() : "<p>No content provided.</p>");
        
        String watermark = request.getWatermarkText() != null && !request.getWatermarkText().trim().isEmpty() 
                ? request.getWatermarkText() 
                : defaultWatermark;
        context.setVariable("watermarkText", watermark);
        
        String docId = request.getDocumentId() != null && !request.getDocumentId().trim().isEmpty()
                ? request.getDocumentId()
                : "DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        context.setVariable("documentId", docId);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        context.setVariable("generatedDate", LocalDateTime.now().format(formatter));

        // Populate certificate specific fields
        context.setVariable("clientName", request.getClientName() != null ? request.getClientName() : "JAYRAMBHAI J RABARI");
        context.setVariable("address", request.getAddress() != null ? request.getAddress() : "PLOT NO. 1084, GIDC, ROAD NO. 4 & 6, SACHIN, SURAT, GUJARAT - 394230, INDIA");
        String std = request.getStandard() != null ? request.getStandard() : "ISO 9001:2015";
        context.setVariable("standard", std);
        String sealStandard = std;
        if (std.contains(":")) {
            sealStandard = std.split(":")[0].trim();
        }
        context.setVariable("sealStandard", sealStandard);
        context.setVariable("systemName", request.getSystemName() != null ? request.getSystemName() : "Quality Management System");
        context.setVariable("scope", request.getScope() != null ? request.getScope() : "TRANSPORT OF BUILDING MATERIALS");
        context.setVariable("certificateNo", request.getCertificateNo() != null ? request.getCertificateNo() : "QMS/26MX894");
        context.setVariable("initialDate", request.getInitialDate() != null ? request.getInitialDate() : "28 May 2026");
        context.setVariable("firstAuditDate", request.getFirstAuditDate() != null ? request.getFirstAuditDate() : "27 May 2027");
        context.setVariable("secondAuditDate", request.getSecondAuditDate() != null ? request.getSecondAuditDate() : "27 May 2028");
        context.setVariable("recertificationDate", request.getRecertificationDate() != null ? request.getRecertificationDate() : "27 May 2029");

        // Static cached assets
        context.setVariable("cssContent", cssContent);
        context.setVariable("logoGlobeBase64", logoGlobeBase64);
        context.setVariable("logoTextBase64", logoTextBase64);
        context.setVariable("sealMqaBase64", sealMqaBase64);
        context.setVariable("sealUkafBase64", sealUkafBase64);
        context.setVariable("sealGoldBase64", sealGoldBase64);

        // Process HTML using Thymeleaf
        String renderedHtml = templateEngine.process("pdf-template", context);

        // Convert HTML to PDF using Flying Saucer
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(renderedHtml);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        }
    }
}
