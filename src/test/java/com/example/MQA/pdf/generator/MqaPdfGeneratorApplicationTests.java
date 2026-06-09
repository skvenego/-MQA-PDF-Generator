package com.example.MQA.pdf.generator;

import com.example.MQA.pdf.generator.model.PdfRequest;
import com.example.MQA.pdf.generator.service.PdfReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;

@SpringBootTest
class MqaPdfGeneratorApplicationTests {

	@Autowired
	private PdfReportService pdfReportService;

	@Test
	void contextLoads() {
	}

	@Test
	void generateSamplePdf() throws Exception {
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
		try (FileOutputStream fos = new FileOutputStream("sample.pdf")) {
			fos.write(pdfBytes);
		}
		System.out.println("Generated sample.pdf successfully!");
	}

}
