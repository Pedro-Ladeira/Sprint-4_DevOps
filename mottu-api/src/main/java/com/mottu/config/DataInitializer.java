package com.mottu.config;

import com.mottu.entity.Modelo;
import com.mottu.entity.Patio;
import com.mottu.repository.ModeloRepository;
import com.mottu.repository.PatioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("h2")
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final ModeloRepository modeloRepository;
    private final PatioRepository patioRepository;

    public DataInitializer(ModeloRepository modeloRepository, PatioRepository patioRepository) {
        this.modeloRepository = modeloRepository;
        this.patioRepository = patioRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        seedModelos();
        seedPatios();
    }

    private void seedModelos() {
        if (modeloRepository.count() == 0) {
            log.info("Seeding sample Modelos into H2 database");
            List<Modelo> modelos = new ArrayList<>();

            Modelo m1 = new Modelo();
            m1.setIdModelo(1);
            m1.setFabricante("Honda");
            m1.setNomeModelo("CB 500");
            m1.setCilindrada(500);
            m1.setTipo("Sport");

            Modelo m2 = new Modelo();
            m2.setIdModelo(2);
            m2.setFabricante("Yamaha");
            m2.setNomeModelo("MT-07");
            m2.setCilindrada(689);
            m2.setTipo("Naked");

            Modelo m3 = new Modelo();
            m3.setIdModelo(3);
            m3.setFabricante("Kawasaki");
            m3.setNomeModelo("Ninja 400");
            m3.setCilindrada(399);
            m3.setTipo("Sport");

            modelos.add(m1);
            modelos.add(m2);
            modelos.add(m3);

            modeloRepository.saveAll(modelos);
        } else {
            log.info("Modelos already present, skipping seed");
        }
    }

    private void seedPatios() {
        if (patioRepository.count() == 0) {
            log.info("Seeding sample Patios into H2 database");
            List<Patio> patios = new ArrayList<>();

            Patio p1 = new Patio();
            p1.setIdPatio(1);
            p1.setNomePatio("Patio Centro");
            p1.setLocalizacaoPatio("Centro");
            p1.setAreaTotal(new BigDecimal("1200.00"));
            p1.setCapacidadeMaxima(100);

            Patio p2 = new Patio();
            p2.setIdPatio(2);
            p2.setNomePatio("Patio Norte");
            p2.setLocalizacaoPatio("Zona Norte");
            p2.setAreaTotal(new BigDecimal("800.00"));
            p2.setCapacidadeMaxima(60);

            patios.add(p1);
            patios.add(p2);

            patioRepository.saveAll(patios);
        } else {
            log.info("Patios already present, skipping seed");
        }
    }
}

