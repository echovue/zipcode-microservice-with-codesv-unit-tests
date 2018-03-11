package com.echovue.configuration;

import com.echovue.service.SOAPYWeatherForecastService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class WeatherForecastConfiguration {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("forecast.wsdl");
        return marshaller;
    }

    @Bean
    public SOAPYWeatherForecastService forecastService(Jaxb2Marshaller marshaller) {
        SOAPYWeatherForecastService forecastService = new SOAPYWeatherForecastService();
        forecastService.setDefaultUri("http://www.webservicex.net/WeatherForecast.asmx");
        forecastService.setMarshaller(marshaller);
        forecastService.setUnmarshaller(marshaller);
        return forecastService;
    }
}
