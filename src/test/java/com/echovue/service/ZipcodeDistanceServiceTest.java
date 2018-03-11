package com.echovue.service;

import com.ca.codesv.engine.junit4.VirtualServerRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.util.Optional;

import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.forGet;
import static com.ca.codesv.protocols.http.fluent.HttpFluentInterface.okMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ZipcodeDistanceServiceTest {
    private String apiKey = "";

    private ZipcodeDistanceService service = new ZipcodeDistanceService();

    @Rule
    public VirtualServerRule vs = new VirtualServerRule();

    @Test
    public void testGetDistance() {
        forGet("http://www.zipcodeapi.com/rest/" + apiKey + "/distance.json/97229/84015/mile")
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