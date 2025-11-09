package com.mottu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "PATIO")
@Data
public class Patio {

    @Id
    @Column(name = "ID_PATIO")
    private Integer idPatio; // INTEGER no banco

    @Column(name = "NOME_PATIO", length = 50)
    private String nomePatio;

    @Column(name = "LOCALIZACAO_PATIO", length = 50)
    private String localizacaoPatio;

    @Column(name = "AREA_TOTAL", precision = 10, scale = 2)
    private BigDecimal areaTotal; // NUMBER(10,2) no banco

    @Column(name = "CAPACIDADE_MAXIMA")
    private Integer capacidadeMaxima;
}
