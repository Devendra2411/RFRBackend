package com.ge.rfr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.ge.rfr"})

public class RfrBackendTest {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RfrBackendTest.class, args);
    }

}
