package megatravel.com.pki.converter;

import megatravel.com.pki.domain.DTO.CertificateDTO;
import megatravel.com.pki.domain.DTO.SubjectDTO;
import megatravel.com.pki.domain.enums.CerType;
import megatravel.com.pki.util.GeneralException;
import org.apache.commons.beanutils.PropertyUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CertificateConverter extends AbstractConverter {

    private static final Map<String, ASN1ObjectIdentifier> x500NameMapper =
            new HashMap<String, ASN1ObjectIdentifier>() {{
        put("commonName", BCStyle.CN);
        put("surname", BCStyle.SURNAME);
        put("givenName", BCStyle.GIVENNAME);
        put("gender", BCStyle.GENDER);
        put("country", BCStyle.C);
        put("email", BCStyle.E);
        put("organization", BCStyle.O);
        put("organizationUnit", BCStyle.OU);
        put("dateOfBirth", BCStyle.DATE_OF_BIRTH);
        put("placeOfBirth", BCStyle.PLACE_OF_BIRTH);
        put("street", BCStyle.STREET);
        put("localityName", BCStyle.L);
        put("postalCode", BCStyle.POSTAL_CODE);
        put("countryOfCitizenship", BCStyle.COUNTRY_OF_CITIZENSHIP);
        put("countryOfResidence", BCStyle.COUNTRY_OF_RESIDENCE);
    }};

    public static List<CertificateDTO> fromMapToDTO(Map<String, String> map) {
        List<CertificateDTO> certificates = new ArrayList<>();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            certificates.add(new CertificateDTO(0L, (String) pair.getKey(),
                    (String) pair.getValue(), true));
            it.remove(); // avoids a ConcurrentModificationException
        }
        return certificates;
    }

    public static X500Name toX500Name(SubjectDTO subject, CerType type) {
        String value;
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);

        for (Field field : subject.getClass().getDeclaredFields()) {
            try {
                value = (String) PropertyUtils.getProperty(subject, field.getName());
                if (value != null && !value.equals("")) {
                    builder.addRDN(x500NameMapper.get(field.getName()), value);
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new GeneralException(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        if (type == CerType.USER) {
            builder.addRDN(BCStyle.UID, System.currentTimeMillis() + "");
        }
        return builder.build();
    }
}
