package megatravel.com.pki.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Set;

@Entity
@DiscriminatorValue("Operator")
public class Operator extends User implements Serializable {

    public Operator() {
    }

    public Operator(Long id, String username, byte[] password, Set<Role> roles) {
        super(id, username, password, roles);
    }
}
