package megatravel.com.pki.domain.DTO;

import megatravel.com.pki.domain.Certificate;

import java.io.Serializable;

public class CertificateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String serialNumber;

    private String distinguishedName;

    private boolean active;

    public CertificateDTO() {
    }

    public CertificateDTO(Long id, String serialNumber, String distinguishedName, boolean active) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.distinguishedName = distinguishedName;
        this.active = active;
    }

    public CertificateDTO(Certificate certificate) {
        this.id = certificate.getId();
        this.serialNumber = certificate.getSerialNumber();
        this.distinguishedName = certificate.getDistinguishedName();
        this.active = certificate.isActive();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
