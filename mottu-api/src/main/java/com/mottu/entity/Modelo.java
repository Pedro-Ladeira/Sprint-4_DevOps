package com.mottu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "MODELO")
@Data
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MODELO")
    private Integer idModelo; // INTEGER no banco

    @Column(name = "FABRICANTE", length = 50)
    private String fabricante;

    @Column(name = "NOME_MODELO", length = 50)
    private String nomeModelo;

    @Column(name = "CILINDRADA")
    private Integer cilindrada;

    @Column(name = "TIPO", length = 30)
    private String tipo;

    // Added: Ano do modelo (opcional)
    @Column(name = "ANO")
    private Integer ano;

    // Relação com Moto (1 Modelo -> N Motos)
    @OneToMany(mappedBy = "modelo", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Moto> motos;
}
