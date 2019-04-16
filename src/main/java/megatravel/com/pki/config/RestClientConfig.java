package megatravel.com.pki.config;

//import org.apache.http.client.HttpClient;
//import org.apache.http.conn.ssl.NoopHostnameVerifier;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.ssl.SSLContexts;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestOperations;
//import org.springframework.web.client.RestTemplate;
//
//import javax.net.ssl.*;
//import java.io.IOException;
//import java.io.InputStream;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.util.Arrays;
//import java.util.stream.Stream;
//
//@Configuration()
//public class RestClientConfig {


//    /** Application keystore path. */
//    @Value("${server.ssl.key-store}")
//    private Resource keystore;
//
//    /** Application keystore type. */
//    @Value("${server.ssl.key-store-type}")
//    private String keystoreType;
//
//    /** Application keystore password. */
//    @Value("${server.ssl.key-store-password}")
//    private String keystorePassword;
//
//    /** Keystore alias for application client credential. */
//    @Value("${server.ssl.key-alias}")
//    private String applicationKeyAlias;
//
//    /** Application truststore path. */
//    @Value("${trust-store}")
//    private Resource truststore;
//
//    /** Application truststore type. */
//    @Value("${trust-store-type}")
//    private String truststoreType;
//
//    /** Application truststore password. */
//    @Value("${trust-store-password}")
//    private String truststorePassword;
//
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(
//                httpClient(keystore, keystorePassword, truststore, truststorePassword)));
//    }
//
////    @Bean()
////    @Qualifier("SSLRestOperations")
////    public RestOperations restOperations(ClientHttpRequestFactory clientHttpRequestFactory) {
////        return new RestTemplate(clientHttpRequestFactory);
////    }
////
////    @Bean
////    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient) {
////        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
////        // timeout
////        requestFactory.setReadTimeout(60 * 1000);
////        requestFactory.setConnectTimeout(60 * 1000);
////        return requestFactory;
////    }
//
//
////        @Bean
////    public RestTemplate restTemplate() {
////        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(createHttpClient(applicationKeyAlias)));
////    }
//
////    private static Resource makeResource(final String path) {
////        if (path.startsWith(FILE_RESOURCE_PREFIX)) {
////            return new FileResource(new File(path.substring(FILE_RESOURCE_PREFIX.length())));
////        } else if (path.startsWith(CLASSPATH_RESOURCE_PREFIX)) {
////            return new ClassPathResource(path.substring(CLASSPATH_RESOURCE_PREFIX.length()));
////        }
////        // Assume a path without a known prefix is a file
////        return new FileResource(new File(path));
////    }
//
////    private HttpClient createHttpClient(final String keyAlias) {
////        final KeyStore trustStore = new KeyStoreFactoryBean(
////                makeResource("classpath:/truststore.jks"), "JKS", "changeit").newInstance();
////        final KeyStore keyStore = new KeyStoreFactoryBean(
////                makeResource(keystorePath), keystoreType, keystorePassword).newInstance();
////        final SSLContext sslContext;
////        try {
////            sslContext = SSLContexts.custom()
////                    .loadKeyMaterial(keyStore, keystorePassword.toCharArray(), (aliases, socket) -> keyAlias)
////                    .loadTrustMaterial(trustStore, (x509Certificates, s) -> false)
////                    .build();
////        } catch (Exception e) {
////            throw new IllegalStateException("Error loading key or trust material", e);
////        }
////        final SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
////                sslContext,
////                new String[] { "TLSv1.2" },
////                null,
////                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
////        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
////                .register("http", PlainConnectionSocketFactory.getSocketFactory())
////                .register("https", sslSocketFactory)
////                .build();
////        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
////        connectionManager.setMaxTotal(httpClientPoolSize);
////        connectionManager.setDefaultMaxPerRoute(httpClientPoolSize);
////        return HttpClients.custom()
////                .setSSLSocketFactory(sslSocketFactory)
////                .setConnectionManager(connectionManager)
////                .build();
////    }
//
//    @Bean
//    public HttpClient httpClient(Resource keystore, String keystorePassword, Resource truststore,
//                                 String truststorePassword) {
//        try {
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            // Using null here initialises the TMF with the default trust store.
//            tmf.init((KeyStore) null);
//
//            // Get hold of the default trust manager
//            X509TrustManager defaultTm = null;
//            for (TrustManager tm : tmf.getTrustManagers()) {
//                if (tm instanceof X509TrustManager) {
//                    defaultTm = (X509TrustManager) tm;
//                    break;
//                }
//            }
//
//            //FileInputStream myKeys = new FileInputStream("truststore.jks");
//
//            // Do the same with your trust store this time
//            // Adapt how you load the keystore to your needs
////            KeyStore myTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
////            myTrustStore.load(myKeys, "password".toCharArray());
//            KeyStore trustStore = KeyStore.getInstance("jks");
//            trustStore.load(truststore.getInputStream(), truststorePassword.toCharArray());
//
//            //myKeys.close();
//
//            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            tmf.init(trustStore);
//
//            // Get hold of the default trust manager
//            X509TrustManager myTm = null;
//            for (TrustManager tm : tmf.getTrustManagers()) {
//                if (tm instanceof X509TrustManager) {
//                    myTm = (X509TrustManager) tm;
//                    break;
//                }
//            }
//
//            // Wrap it in your own class.
//            final X509TrustManager finalDefaultTm = defaultTm;
//            final X509TrustManager finalMyTm = myTm;
//            X509TrustManager customTm = new X509TrustManager() {
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    // If you're planning to use client-cert auth,
//                    // merge results from "defaultTm" and "myTm".
//                    if (finalMyTm != null && finalDefaultTm != null) {
//                        return Stream.concat(Arrays.stream(finalMyTm.getAcceptedIssuers())
//                                , Arrays.stream(finalDefaultTm.getAcceptedIssuers()))
//                                .toArray(X509Certificate[]::new);
//                    }
//                    return new X509Certificate[] {};
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] chain,
//                                               String authType) throws CertificateException {
//                    try {
//                        if (finalMyTm != null) {
//                            finalMyTm.checkServerTrusted(chain, authType);
//                        } else {
//                            throw new CertificateException();
//                        }
//                    } catch (CertificateException e) {
//                        // This will throw another CertificateException if this fails too.
//                        if (finalDefaultTm != null) {
//                            finalDefaultTm.checkServerTrusted(chain, authType);
//                        } else {
//                            throw new CertificateException();
//                        }
//                    }
//                }
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] chain,
//                                               String authType) throws CertificateException {
//                    // If you're planning to use client-cert auth,
//                    // do the same as checking the server.
//                    try {
//                        if (finalMyTm != null) {
//                            finalMyTm.checkServerTrusted(chain, authType);
//                        } else {
//                            throw new CertificateException();
//                        }
//                    } catch (CertificateException e) {
//                        // This will throw another CertificateException if this fails too.
//                        if (finalDefaultTm != null) {
//                            finalDefaultTm.checkServerTrusted(chain, authType);
//                        } else {
//                            throw new CertificateException();
//                        }
//                    }
//                }
//            };
//
//            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            //TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//
//            KeyStore keyStore = KeyStore.getInstance("jks");
//            keyStore.load(keystore.getInputStream(), keystorePassword.toCharArray());
//
//            kmf.init(keyStore, keystorePassword.toCharArray());
//            //tmf.init(trustStore);
//
//
////            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
////                    .loadKeyMaterial(keyStore, keystorePassword.toCharArray()).build();
////            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, (x509Certificates, s) -> false)
////                    .loadKeyMaterial(keyStore, keystorePassword.toCharArray()).build();
////            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.2"},
////                    null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
//
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, new TrustManager[] { customTm }, null);
//            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1.2"},
//                    null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
//
//            return HttpClients.custom()//.setSSLHostnameVerifier(new NoopHostnameVerifier()) // HostnameVerifier NoopHostnameVerifier
//                    .setSSLSocketFactory(sslFactory)
//                    .build();
//        } catch (Exception e) {
//            throw new IllegalStateException("Error while configuring SSL rest template", e);
//    }
//        }
//}