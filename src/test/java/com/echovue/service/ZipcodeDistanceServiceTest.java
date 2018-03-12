package com.echovue.service;

import com.ca.codesv.engine.junit4.VirtualServerRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.MediaType;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Optional;

import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.forGet;
import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.okMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ZipcodeDistanceServiceTest {

    private ZipcodeDistanceService service = new ZipcodeDistanceService();

    @Rule
    public VirtualServerRule vs = new VirtualServerRule();

    private TrustManager[] trustAllCerts;
    private SSLContext sslContext;

    @Before
    public void setUp() throws Exception {
        // using own trust manager
        trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                        if (certs.length != 1 || !certs[0].getIssuerX500Principal().getName()
                                .contains("CN=www.zipcodeapi.com")) {
                            throw new SecurityException("Invalid certificate");
                        }
                    }
                }
        };

        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());
    }

    @Test(timeout = 10000L)
    public void testGetDistance() throws IOException {
        final String URL = "http://www.zipcodeapi.com/rest/" + service.apiKey + "/distance.json/97229/84015/mile";

        forGet(URL)
            .doReturn(
                okMessage()
                    .withJsonBody("{\"distance\":620.755}")
                    .withContentType(MediaType.APPLICATION_JSON.toString())
            );

        Optional<Double> response = service.getDistance("97229", "84015");

        assertTrue(response.isPresent());
        assertEquals(new Double("620.755"), response.get());
    }
}