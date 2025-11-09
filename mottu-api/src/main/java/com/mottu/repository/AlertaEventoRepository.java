package com.mottu.repository;

import com.mottu.entity.AlertaEvento;
import com.mottu.entity.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlertaEventoRepository extends JpaRepository<AlertaEvento, Long> {

    // Busca por tipo de alerta
    List<AlertaEvento> findByTipoAlerta(String tipoAlerta);

    // Busca por moto (Moto entity)
    List<AlertaEvento> findByMoto(Moto moto);

    // Busca por data de geração (LocalDate - DATE no banco)
    List<AlertaEvento> findByDataGeracao(LocalDate dataGeracao);
    List<AlertaEvento> findByDataGeracaoAfter(LocalDate data);
    List<AlertaEvento> findByDataGeracaoBefore(LocalDate data);
    List<AlertaEvento> findByDataGeracaoBetween(LocalDate inicio, LocalDate fim);

    // Busca por tipo e moto
    List<AlertaEvento> findByTipoAlertaAndMoto(String tipoAlerta, Moto moto);

    // Ordenação por data mais recente
    List<AlertaEvento> findAllByOrderByDataGeracaoDesc();
    List<AlertaEvento> findByMotoOrderByDataGeracaoDesc(Moto moto);
    List<AlertaEvento> findByTipoAlertaOrderByDataGeracaoDesc(String tipoAlerta);

    // Contadores
    long countByTipoAlerta(String tipoAlerta);
    long countByMoto(Moto moto);
    long countByDataGeracaoAfter(LocalDate data);

    // Delete por data (LocalDate)
    @Modifying
    @Query("DELETE FROM AlertaEvento a WHERE a.dataGeracao < :dataLimite")
    void deleteByDataGeracaoBefore(@Param("dataLimite") LocalDate dataLimite);

    // Delete por moto
    @Modifying
    @Query("DELETE FROM AlertaEvento a WHERE a.moto = :moto")
    void deleteByMoto(@Param("moto") Moto moto);
}
