package com.ge.rfr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration(exclude = FreeMarkerAutoConfiguration.class)
@ComponentScan(basePackageClasses = RfrBackend.class)
@Configuration
public class RfrBackend {

    public static void main(String[] args) throws Exception {
    	SpringApplication.run(RfrBackend.class, args);
    }

}
