package com.mottu.repository;

import com.mottu.entity.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Integer> { // INTEGER ID

    List<Modelo> findByFabricante(String fabricante);

    Optional<Modelo> findByNomeModelo(String nomeModelo);

    List<Modelo> findByTipo(String tipo);

    List<Modelo> findByCilindrada(Integer cilindrada);

    List<Modelo> findByCilindradaBetween(Integer min, Integer max);

    @Query("SELECT m FROM Modelo m JOIN Moto mo ON m.idModelo = mo.modelo.idModelo GROUP BY m.idModelo, m.nomeModelo, m.fabricante ORDER BY COUNT(mo.idMoto) DESC")
    List<Modelo> findModelosMaisUtilizados();

    @Query("SELECT COUNT(m) FROM Moto m WHERE m.modelo.idModelo = :modeloId")
    long countMotosByModeloId(Integer modeloId); // INTEGER ID
}
