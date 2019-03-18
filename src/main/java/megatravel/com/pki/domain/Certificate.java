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

    public Certificate() {
    }

    public Certificate(Long id, String serialNumber, String distinguishedName) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.distinguishedName = distinguishedName;
    }

    public Certificate(CertificateDTO certificate) {
        this.id = certificate.getId();
        this.serialNumber = certificate.getSerialNumber();
        this.distinguishedName = certificate.getDistinguishedName();
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
}
