package megatravel.com.pki.repository;

import megatravel.com.pki.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    public Role findRoleByName(String name);

}
