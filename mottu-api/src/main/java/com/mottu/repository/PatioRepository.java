package com.mottu.repository;

import com.mottu.entity.Patio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatioRepository extends JpaRepository<Patio, Integer> { // INTEGER ID

    Optional<Patio> findByNomePatio(String nomePatio);

    List<Patio> findByLocalizacaoPatio(String localizacao);

    List<Patio> findByLocalizacaoPatioContainingIgnoreCase(String localizacao);

    List<Patio> findByCapacidadeMaxima(Integer capacidade);
    List<Patio> findByCapacidadeMaximaGreaterThanEqual(Integer capacidadeMinima);

    List<Patio> findByAreaTotalBetween(BigDecimal areaMin, BigDecimal areaMax);

    @Query("SELECT p FROM Patio p WHERE (SELECT COUNT(m) FROM Moto m WHERE m.patio.idPatio = p.idPatio) < p.capacidadeMaxima ORDER BY (p.capacidadeMaxima - (SELECT COUNT(m) FROM Moto m WHERE m.patio.idPatio = p.idPatio)) DESC")
    List<Patio> findPatiosComEspacoDisponivel();

    @Query("SELECT COUNT(m) FROM Moto m WHERE m.patio.idPatio = :patioId")
    long countMotosByPatioId(Integer patioId); // INTEGER ID

    @Query("SELECT CAST((COUNT(m) * 100.0 / p.capacidadeMaxima) AS BigDecimal) FROM Patio p LEFT JOIN Moto m ON m.patio.idPatio = p.idPatio WHERE p.idPatio = :patioId GROUP BY p.idPatio, p.capacidadeMaxima")
    BigDecimal calcularTaxaOcupacao(Integer patioId); // Retorna BigDecimal
}
