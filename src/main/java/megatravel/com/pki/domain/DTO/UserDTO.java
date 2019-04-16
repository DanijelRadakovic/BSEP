package megatravel.com.pki.domain.DTO;


import megatravel.com.pki.converter.AbstractConverter;

import java.util.Set;

public class UserDTO extends AbstractConverter {

    private String username;

    private Set<RoleDTO> roles;

    public UserDTO() {
    }

    public UserDTO(String username, String password, Set<RoleDTO> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }
}
