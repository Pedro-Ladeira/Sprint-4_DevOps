package com.mottu.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EndpointLogger implements CommandLineRunner {

    @Autowired
    private RequestMappingHandlerMapping mapping;

    private static final Logger logger = LoggerFactory.getLogger(EndpointLogger.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("=== Registered Request Mappings ===");
        mapping.getHandlerMethods().forEach((k, v) -> logger.debug("{} => {}", k, v));
        logger.info("=== End Request Mappings ===");
    }
}
