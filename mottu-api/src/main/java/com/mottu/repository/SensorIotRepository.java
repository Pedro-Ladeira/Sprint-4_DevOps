package com.mottu.repository;

import com.mottu.entity.Moto;
import com.mottu.entity.SensorIot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SensorIotRepository extends JpaRepository<SensorIot, Integer> {

    List<SensorIot> findByTipoSensor(String tipoSensor);

    List<SensorIot> findByDataTransmissao(LocalDate dataTransmissao);

    List<SensorIot> findByBateriaPercentualBetween(BigDecimal min, BigDecimal max);

    List<SensorIot> findByBateriaPercentualLessThan(BigDecimal limite);

    List<SensorIot> findByDataTransmissaoAfter(LocalDate dataLimite);

    @Query("SELECT s FROM SensorIot s WHERE s.idMoto IS NULL")
    List<SensorIot> findSensoresSemMoto();

    @Modifying
    @Query("UPDATE SensorIot s SET s.idMoto = NULL WHERE s.idMoto = :idMoto")
    void desassociarMotoById(@Param("idMoto") Integer idMoto);
}
