package megatravel.com.pki.repository;

import megatravel.com.pki.domain.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    @Query(value = "SELECT * FROM certificate", nativeQuery = true)
    List<Certificate> findBySerialNumber();
}
