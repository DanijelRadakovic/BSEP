package megatravel.com.pki.domain.DTO;

import megatravel.com.pki.domain.Server;
import megatravel.com.pki.domain.enums.ServerType;

import java.io.Serializable;
import java.net.URL;

public class ServerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private URL address;

    private ServerType serverType;

    public ServerDTO() {
    }

    public ServerDTO(Long id, URL address, ServerType serverType) {
        this.id = id;
        this.address = address;
        this.serverType = serverType;
    }

    public ServerDTO(Server server) {
        this.id = server.getId();
        this.address = server.getAddress();
        this.serverType = server.getType();
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

    public URL getAddress() {
        return address;
    }

    public void setAddress(URL address) {
        this.address = address;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }
}
