package com.echovue.service;

import forecast.wsdl.GetWeatherByZipCode;
import forecast.wsdl.GetWeatherByZipCodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

@Service
public class SOAPYWeatherForecastService extends WebServiceGatewaySupport {
    public static final Logger LOGGER = LoggerFactory.getLogger(SOAPYWeatherForecastService.class);

    public GetWeatherByZipCodeResponse getForecast(final String zipCode) {
        GetWeatherByZipCode request = new GetWeatherByZipCode();
        request.setZipCode(zipCode);
        LOGGER.info("**** Calling for Forecast - " + zipCode);
        GetWeatherByZipCodeResponse forecast = (GetWeatherByZipCodeResponse)  getWebServiceTemplate()
                .marshalSendAndReceive("http://www.webservicex.net/WeatherForecast.asmx",
                        request,
                        new SoapActionCallback("http://www.webserviceX.NET/WeatherForecast"));
        return  forecast;
    }

}
