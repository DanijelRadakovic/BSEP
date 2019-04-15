package megatravel.com.pki.repository;

import megatravel.com.pki.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findUserByUsername(String username);

}
