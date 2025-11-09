package com.mottu.repository;

import com.mottu.entity.HistoricoPosicao;
import com.mottu.entity.HistoricoPosicaoId;
import com.mottu.entity.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoPosicaoRepository extends JpaRepository<HistoricoPosicao, HistoricoPosicaoId> {
    @Modifying
    @Query("DELETE FROM HistoricoPosicao h WHERE h.id.moto = :moto")
    void deleteByMoto(@Param("moto") Moto moto);
}
