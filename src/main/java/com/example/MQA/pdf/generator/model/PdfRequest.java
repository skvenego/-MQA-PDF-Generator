package com.example.MQA.pdf.generator.model;

public class PdfRequest {
    // Original fields for standard PDF generation
    private String title;
    private String subtitle;
    private String content;
    private String watermarkText;
    private String documentId;

    // Certificate of Registration specific fields
    private String clientName;
    private String address;
    private String standard;
    private String systemName;
    private String scope;
    private String certificateNo;
    private String initialDate;
    private String firstAuditDate;
    private String secondAuditDate;
    private String recertificationDate;

    public PdfRequest() {}

    public PdfRequest(String title, String subtitle, String content, String watermarkText, String documentId,
                      String clientName, String address, String standard, String systemName, String scope,
                      String certificateNo, String initialDate, String firstAuditDate, String secondAuditDate,
                      String recertificationDate) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.watermarkText = watermarkText;
        this.documentId = documentId;
        this.clientName = clientName;
        this.address = address;
        this.standard = standard;
        this.systemName = systemName;
        this.scope = scope;
        this.certificateNo = certificateNo;
        this.initialDate = initialDate;
        this.firstAuditDate = firstAuditDate;
        this.secondAuditDate = secondAuditDate;
        this.recertificationDate = recertificationDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getWatermarkText() { return watermarkText; }
    public void setWatermarkText(String watermarkText) { this.watermarkText = watermarkText; }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStandard() { return standard; }
    public void setStandard(String standard) { this.standard = standard; }

    public String getSystemName() { return systemName; }
    public void setSystemName(String systemName) { this.systemName = systemName; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }

    public String getCertificateNo() { return certificateNo; }
    public void setCertificateNo(String certificateNo) { this.certificateNo = certificateNo; }

    public String getInitialDate() { return initialDate; }
    public void setInitialDate(String initialDate) { this.initialDate = initialDate; }

    public String getFirstAuditDate() { return firstAuditDate; }
    public void setFirstAuditDate(String firstAuditDate) { this.firstAuditDate = firstAuditDate; }

    public String getSecondAuditDate() { return secondAuditDate; }
    public void setSecondAuditDate(String secondAuditDate) { this.secondAuditDate = secondAuditDate; }

    public String getRecertificationDate() { return recertificationDate; }
    public void setRecertificationDate(String recertificationDate) { this.recertificationDate = recertificationDate; }

    // Static Builder Class
    public static class Builder {
        private String title;
        private String subtitle;
        private String content;
        private String watermarkText;
        private String documentId;
        private String clientName;
        private String address;
        private String standard;
        private String systemName;
        private String scope;
        private String certificateNo;
        private String initialDate;
        private String firstAuditDate;
        private String secondAuditDate;
        private String recertificationDate;

        public Builder title(String title) { this.title = title; return this; }
        public Builder subtitle(String subtitle) { this.subtitle = subtitle; return this; }
        public Builder content(String content) { this.content = content; return this; }
        public Builder watermarkText(String watermarkText) { this.watermarkText = watermarkText; return this; }
        public Builder documentId(String documentId) { this.documentId = documentId; return this; }
        public Builder clientName(String clientName) { this.clientName = clientName; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder standard(String standard) { this.standard = standard; return this; }
        public Builder systemName(String systemName) { this.systemName = systemName; return this; }
        public Builder scope(String scope) { this.scope = scope; return this; }
        public Builder certificateNo(String certificateNo) { this.certificateNo = certificateNo; return this; }
        public Builder initialDate(String initialDate) { this.initialDate = initialDate; return this; }
        public Builder firstAuditDate(String firstAuditDate) { this.firstAuditDate = firstAuditDate; return this; }
        public Builder secondAuditDate(String secondAuditDate) { this.secondAuditDate = secondAuditDate; return this; }
        public Builder recertificationDate(String recertificationDate) { this.recertificationDate = recertificationDate; return this; }

        public PdfRequest build() {
            return new PdfRequest(title, subtitle, content, watermarkText, documentId,
                    clientName, address, standard, systemName, scope,
                    certificateNo, initialDate, firstAuditDate, secondAuditDate, recertificationDate);
        }
    }
}
