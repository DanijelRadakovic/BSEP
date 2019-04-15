package megatravel.com.pki.converter;

import megatravel.com.pki.domain.DTO.CertificateDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CertificateConverter extends AbstractConverter {

    public static List<CertificateDTO> fromMapToDTO(Map<String, String> map) {
        List<CertificateDTO> certificates = new ArrayList<>();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            certificates.add(new CertificateDTO(null, (String) pair.getKey(),
                    (String) pair.getValue(), true));
            it.remove(); // avoids a ConcurrentModificationException
        }
        return certificates;
    }
}
