package com.ge.rfr.common;

import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers additional modules in the Jackson Object Mapper used by Spring Boot.
 * <p>
 * Please note the Spring Boot documentation states that all beans of type Module will be
 * auto-registered with the mapper.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public GuavaModule guavaModule() {
        return new GuavaModule();
    }

    /**
     * Customize the Spring MVC ObjectMapper to re-enable the default-inclusion of unannotated fields
     * when JSON views are being used.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer enableViewInclusion() {
        return builder -> builder.defaultViewInclusion(true);
    }

}
