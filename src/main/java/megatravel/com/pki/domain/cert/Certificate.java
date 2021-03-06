package megatravel.com.pki.domain.cert;

import megatravel.com.pki.domain.enums.CerType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Certificate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String serialNumber;

    @Column(unique = true)
    private String distinguishedName;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private CerType type;

    @Column(nullable = false)
    private boolean active;

    public Certificate() {
    }

    public Certificate(Long id, String serialNumber, String distinguishedName, CerType type, boolean active) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.distinguishedName = distinguishedName;
        this.type = type;
        this.active = active;
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

    public CerType getType() {
        return type;
    }

    public void setType(CerType type) {
        this.type = type;
    }
}
