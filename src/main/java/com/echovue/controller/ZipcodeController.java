package com.echovue.controller;

import com.echovue.service.SOAPYWeatherForecastService;
import com.echovue.service.ZipcodeDistanceService;
import forecast.wsdl.GetWeatherByZipCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ZipcodeController {

    private ZipcodeDistanceService distanceService;
    private SOAPYWeatherForecastService forcastService;

    private static final String MILES = "miles";

    @Autowired
    public ZipcodeController(ZipcodeDistanceService distanceService,
                             SOAPYWeatherForecastService forecastService) {
        this.distanceService = distanceService;
        this.forcastService = forecastService;
    }

    @RequestMapping(value = "/distance/{zipCode1}/{zipCode2}", method = GET)
    public String getDistance(@PathVariable final String zipCode1,
                              @PathVariable final String zipCode2) {
        Optional<Double> distanceResult =
                distanceService.getDistance(zipCode1, zipCode2);
        if (distanceResult.isPresent()) {
            return Math.round(distanceResult.get()) + " " + MILES;
        }
        return "Unable to calculate distance";
    }

    @RequestMapping(value = "/weather/{zipCode}", method = GET)
    public GetWeatherByZipCodeResponse getForecast(@PathVariable final String zipCode) {
        return forcastService.getForecast(zipCode);
    }
}
