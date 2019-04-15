package megatravel.com.pki.domain;

import megatravel.com.pki.domain.enums.AdminType;
import megatravel.com.pki.domain.enums.UserType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@DiscriminatorValue("Admin")
public class Admin extends User implements Serializable {


    public Admin() {
    }

    public Admin(Long id, String username, byte[] password, Set<Role> roles) {
        super(id, username, password, roles);
    }
}

