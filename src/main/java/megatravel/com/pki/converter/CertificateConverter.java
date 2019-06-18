package megatravel.com.pki.converter;

import megatravel.com.pki.domain.DTO.CertificateDTO;
import megatravel.com.pki.domain.DTO.CertificateDistributionDTO;
import megatravel.com.pki.domain.DTO.SubjectDTO;
import megatravel.com.pki.domain.cert.CertificateDistribution;
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
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;

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
                put("placeOfBirth", BCStyle.PLACE_OF_BIRTH);
                put("street", BCStyle.STREET);
                put("localityName", BCStyle.L);
                put("postalCode", BCStyle.POSTAL_CODE);
                put("countryOfCitizenship", BCStyle.COUNTRY_OF_CITIZENSHIP);
                put("countryOfResidence", BCStyle.COUNTRY_OF_RESIDENCE);
            }};

    public static List<CertificateDTO> fromListX509ToDTO(List<X509Certificate> certs) {
         return certs.stream().map(cert -> new CertificateDTO(0L, cert.getSerialNumber().toString(),
                cert.getSubjectDN().toString(), true)).collect(Collectors.toList());
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

        if (type == CerType.CLIENT) {
            builder.addRDN(BCStyle.UID, System.currentTimeMillis() + "");
        }
        return builder.build();
    }

    public static CertificateDistribution toEntity(CertificateDistributionDTO distribution) {
        return new CertificateDistribution(distribution.getSerialNumber(), distribution.isPrivateKey(),
                distribution.isKeystore(), distribution.isTruststore(), distribution.getHostname(),
                distribution.getDestination());
    }
}
