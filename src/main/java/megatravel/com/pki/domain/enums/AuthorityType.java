package megatravel.com.pki.domain.enums;

import org.springframework.security.core.GrantedAuthority;

public enum AuthorityType implements GrantedAuthority {
    REGISTERED_USER,
    ADMIN,
    VALIDATOR,
    OPERATER;

    public String getAuthority() {
        return name();
    }
}
