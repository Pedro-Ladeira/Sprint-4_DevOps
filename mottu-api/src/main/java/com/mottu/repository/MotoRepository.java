package com.mottu.repository;

import com.mottu.entity.Moto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MotoRepository extends JpaRepository<Moto, Integer> {

    // Busca por placa
    Optional<Moto> findByPlaca(String placa);
    
    // Busca por chassi
    Optional<Moto> findByChassi(String chassi);
    
    // Busca por placa contendo (case insensitive)
    Page<Moto> findByPlacaContainingIgnoreCase(String placa, Pageable pageable);
    
    // Busca por status
    List<Moto> findByStatus(String status);
    
    // Busca por pátio
    List<Moto> findByPatio_IdPatio(Integer patioId);

    // Busca por modelo
    List<Moto> findByModelo_IdModelo(Integer modeloId);

    // Contadores
    long countByStatus(String status);
    long countByPatio_IdPatio(Integer patioId);

    // Query customizada para dashboard
    @Query("SELECT m FROM Moto m WHERE m.status = :status AND m.patio.idPatio = :patioId")
    List<Moto> findByStatusAndPatio(@Param("status") String status, @Param("patioId") Integer patioId);

    // Query para motos com sensor IoT
    @Query("SELECT m FROM Moto m WHERE m.sensorIot IS NOT NULL")
    List<Moto> findMotosComSensor();
    
    // Query para motos sem sensor IoT
    @Query("SELECT m FROM Moto m WHERE m.sensorIot IS NULL")
    List<Moto> findMotosSemSensor();
    
    // Busca por ano de fabricação
    List<Moto> findByAnoFabricacao(Integer ano);
    List<Moto> findByAnoFabricacaoBetween(Integer anoInicio, Integer anoFim);
}