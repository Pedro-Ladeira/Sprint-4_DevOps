package com.mottu.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebViewRoutes implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Map root to home
        registry.addViewController("/").setViewName("home/index");

        // Mapeia rotas “páginas” para templates thymeleaf correspondentes
        registry.addViewController("/dashboard").setViewName("dashboard/index");
        registry.addViewController("/modelos").setViewName("modelo/modelo-list");
        registry.addViewController("/motos").setViewName("moto/list");
        registry.addViewController("/patios").setViewName("patio/patio-list");
        registry.addViewController("/usuarios").setViewName("usuario/usuario-list");
        registry.addViewController("/sensores").setViewName("sensor/sensor-list");
        registry.addViewController("/historico").setViewName("historico/historico-list");
        registry.addViewController("/alertas").setViewName("alerta/alerta-list");
        registry.addViewController("/login").setViewName("auth/login");
        registry.addViewController("/home").setViewName("home/index");
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}
