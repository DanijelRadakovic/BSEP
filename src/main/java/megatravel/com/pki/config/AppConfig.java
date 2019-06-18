package megatravel.com.pki.config;

import megatravel.com.pki.domain.enums.PeriodUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${pki.keystore}")
    private String keystore;

    @Value("${pki.keystore.password}")
    private String keystorePassword;

    @Value("${pki.truststore.dir}")
    private String truststore;

    @Value("${pki.truststore.password}")
    private String truststorePassword;

    @Value("${pki.certificates.dir}")
    private String certificates;

    @Value("${pki.certificate.provider}")
    private String provider;

    @Value("${pki.algorithm.signature}")
    private String signatureAlgorithm;

    @Value("${pki.algorithm.key}")
    private String keyAlgorithm;

    @Value("${pki.seed.algorithm}")
    private String seedAlgorithm;

    @Value("${pki.seed.provider}")
    private String seedProvider;

    @Value("${pki.period.unit}")
    private String periodUnit;

    @Value("${pki.client.keysize}")
    private String clientKeysize;

    @Value("${pki.client.period}")
    private String clientPeriod;

    @Value("${pki.ca.keysize}")
    private String caKeySize;

    @Value("${pki.ca.period}")
    private String caPeriod;

    @Value("${pki.ssh.username}")
    private String sshUsername;

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getTruststore() {
        return truststore;
    }

    public void setTruststore(String truststore) {
        this.truststore = truststore;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    public void setKeyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
    }

    public String getSeedAlgorithm() {
        return seedAlgorithm;
    }

    public void setSeedAlgorithm(String seedAlgorithm) {
        this.seedAlgorithm = seedAlgorithm;
    }

    public String getSeedProvider() {
        return seedProvider;
    }

    public void setSeedProvider(String seedProvider) {
        this.seedProvider = seedProvider;
    }

    public PeriodUnit getPeriodUnit() {
        return PeriodUnit.factory(periodUnit);
    }

    public void setPeriodUnit(PeriodUnit periodUnit) {
        this.periodUnit = periodUnit.toString();
    }

    public int getClientKeysize() {
        return Integer.parseInt(clientKeysize);
    }

    public void setClientKeysize(int clientKeysize) {
        this.clientKeysize = clientKeysize + "";
    }

    public int getClientPeriod() {
        return Integer.parseInt(clientPeriod);
    }

    public void setClientPeriod(int clientPeriod) {
        this.clientPeriod = clientPeriod + "";
    }

    public int getCaKeySize() {
        return Integer.parseInt(caKeySize);
    }

    public void setCaKeySize(int caKeySize) {
        this.caKeySize = caKeySize + "";
    }

    public int getCaPeriod() {
        return Integer.parseInt(caPeriod);
    }

    public void setCaPeriod(int caPeriod) {
        this.caPeriod = caPeriod + "";
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public void setTruststorePassword(String truststorePassword) {
        this.truststorePassword = truststorePassword;
    }

    public String getSshUsername() {
        return sshUsername;
    }

    public void setSshUsername(String sshUsername) {
        this.sshUsername = sshUsername;
    }
}
