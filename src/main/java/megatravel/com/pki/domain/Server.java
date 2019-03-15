package megatravel.com.pki.domain;

import megatravel.com.pki.domain.DTO.ServerDTO;
import megatravel.com.pki.domain.enums.ServerType;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;

@Entity
public class Server implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private URL address;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ServerType type;

    public Server() {
    }

    public Server(Long id, URL address, ServerType type) {
        this.id = id;
        this.address = address;
        this.type = type;
    }

    public Server(ServerDTO server) {
        this.id = server.getId();
        this.address = server.getAddress();
        this.type = server.getServerType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public URL getAddress() {
        return address;
    }

    public void setAddress(URL address) {
        this.address = address;
    }

    public ServerType getType() {
        return type;
    }

    public void setType(ServerType type) {
        this.type = type;
    }
}
