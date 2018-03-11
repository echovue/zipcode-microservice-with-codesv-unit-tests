package com.echovue.service;

import com.echovue.model.Distance;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ZipcodeDistanceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipcodeDistanceService.class);
    private URL zipcodeapi;
    private String apiKey = "";

    public Optional<Double> getDistance(final String zipCode1,
                                        final String zipCode2) {
        try {
            zipcodeapi = new URL("http://www.zipcodeapi.com/rest/"
                                    + apiKey + "/distance.json/"
                                    + zipCode1 + "/" + zipCode2
                                    + "/mile");
            System.out.println(zipcodeapi);
            RestTemplate restTemplate = new RestTemplate();

            return Optional.of(restTemplate.getForObject(
                    zipcodeapi.toString(), Distance.class).getDistance());

        } catch (MalformedURLException urlException) {
            LOGGER.error("Invalid URL for ZipCodeApi");
        } catch (org.springframework.web.client.HttpClientErrorException ex) {
            LOGGER.error("Bad request");
        }
        return Optional.empty();
    }
}
