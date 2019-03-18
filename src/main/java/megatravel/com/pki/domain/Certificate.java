package megatravel.com.pki.domain;

import megatravel.com.pki.domain.DTO.CertificateDTO;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Certificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column
    private String distinguishedName;

    @Column(nullable = false)
    private boolean active;

    public Certificate() {
    }

    public Certificate(Long id, String serialNumber, String distinguishedName, boolean active) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.distinguishedName = distinguishedName;
        this.active = active;
    }

    public Certificate(CertificateDTO certificate) {
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
