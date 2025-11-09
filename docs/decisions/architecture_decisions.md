# Architecture Decisions

Document key architecture decisions (ADR):

- Use Spring Boot for quick REST API and integration with JPA.
- Use Docker for deployment; keep DB external to avoid heavy images like Oracle.
- Use DTOs to decouple entities from API contracts.
- Use Controller -> Service -> Repository separation for single responsibility.

Add more ADRs as decisions are made.
name: CI

on:
  push:
    branches: [ main, master ]
  pull_request:
    branches: [ main, master ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: temurin
      - name: Build with Maven
        run: mvn -B -f mottu-api/pom.xml clean verify
-- Seed data for demo (insert minimal records)
-- Adjust names and schema if necessary

INSERT INTO MODELO (ID, NOME) VALUES (1, 'CG 160');
INSERT INTO MOTO (ID, PLACA, MODELO_ID) VALUES (1, 'ABC1234', 1);
package com.mottu.config;

import org.springframework.context.annotation.Configuration;

// Placeholder for actuator configuration if needed
@Configuration
public class ActuatorConfig {
    // Actuator endpoints are auto-configured when dependency is present
}
package com.mottu.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("motos","modelos","patios","usuarios");
    }
}
package com.mottu.config;

public final class Constants {
    private Constants() {}

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;
}
package com.mottu.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(NotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage() == null ? "Resource not found" : ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage() == null ? "Bad request" : ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAll(Exception ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Internal server error");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
package com.mottu.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
target/
*.log
*.tmp
*.jar
*.war
.DS_Store
.idea/
.vscode/
*.iml
.mvn/
.settings/
node_modules/
.env
docker/.env

