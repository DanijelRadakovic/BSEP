package megatravel.com.pki.domain.DTO;

import megatravel.com.pki.domain.enums.CerType;

public class CertificateRequestDTO {
    private SubjectDTO subjectDN;
    private String issuerSN;
    private CerType type;

    public CertificateRequestDTO() {
    }

    public CertificateRequestDTO(SubjectDTO subjectDN, String issuerSN, CerType type) {
        this.subjectDN = subjectDN;
        this.issuerSN = issuerSN;
        this.type = type;
    }

    public SubjectDTO getSubjectDN() {
        return subjectDN;
    }

    public void setSubjectDN(SubjectDTO subjectDN) {
        this.subjectDN = subjectDN;
    }

    public String getIssuerSN() {
        return issuerSN;
    }

    public void setIssuerSN(String issuerSN) {
        this.issuerSN = issuerSN;
    }

    public CerType getType() {
        return type;
    }

    public void setType(CerType type) {
        this.type = type;
    }
}
