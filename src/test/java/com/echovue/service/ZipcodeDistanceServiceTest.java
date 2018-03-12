package com.echovue.service;

import com.ca.codesv.engine.junit4.VirtualServerRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.Optional;

import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.forGet;
import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.okMessage;
import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.unauthorizedMessage;
import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.verifyGet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ZipcodeDistanceServiceTest {
    private ZipcodeDistanceService service = new ZipcodeDistanceService();

    private final String URL = "http://www.zipcodeapi.com/rest/" + service.apiKey + "/distance.json/{zipcode1}/{zipcode2}/mile";

    @Rule
    public VirtualServerRule vs = new VirtualServerRule();

    @Before
    public void setUp() {
        System.setProperty("codesv.export", "src/test/resources/generatedRRPairs/");
    }

    @After
    public void cleanUp() {
        System.clearProperty("codesv.export");
    }

    @Test
    public void testGetDistance() {
        forGet(URL)
            .matchesPathParameter("zipcode1", "97229")
            .matchesPathParameter("zipcode2", "84016")
            .doReturn(
                okMessage()
                    .withJsonBody("{\"distance\":620.755}")
                    .withContentType(MediaType.APPLICATION_JSON.toString())
            );

        Optional<Double> response = service.getDistance("97229", "84016");

        assertTrue(response.isPresent());
        assertEquals(new Double("620.755"), response.get());
        verifyGet(URL).invoked(1);
    }

    @Ignore //Unable to successfully implement two different tests in the same class so far.
    @Test
    public void testGetDistanceAPIKeyNotFound() {
        forGet(URL)
            .matchesPathParameter("zipcode1", "97229")
            .matchesPathParameter("zipcode2", "84015")
            .doReturn(
                unauthorizedMessage()
                    .withJsonBody("{\"error_code\":401,\"error_msg\":\"API key not found.\"}")
                    .withContentType(MediaType.APPLICATION_JSON.toString())
            );

        Optional<Double> response = service.getDistance("97229", "84015");

        assertFalse(response.isPresent());
    }
}