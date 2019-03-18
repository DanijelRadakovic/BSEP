package megatravel.com.pki.domain.DTO;

import java.io.Serializable;

public class IssuerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String serialNumber;

    private String x500Name;

    public IssuerDTO() {
    }

    public IssuerDTO(String serialNumber, String x500Name) {
        this.serialNumber = serialNumber;
        this.x500Name = x500Name;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getX500Name() {
        return x500Name;
    }

    public void setX500Name(String x500Name) {
        this.x500Name = x500Name;
    }
}
