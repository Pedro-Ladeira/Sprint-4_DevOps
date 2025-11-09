package com.mottu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "MOTO")
@Data
public class Moto {

    @Id
    @Column(name = "ID_MOTO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMoto;

    @Column(name = "PLACA", length = 10)
    private String placa;

    @Column(name = "CHASSI", length = 50)
    private String chassi;

    @Column(name = "ANO_FABRICACAO")
    private Integer anoFabricacao;

    @Column(name = "STATUS", length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODELO_ID_MODELO")
    private Modelo modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PATIO")
    private Patio patio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SENSOR_IOT")
    private SensorIot sensorIot;

    @Column(name = "DATA_ATUALIZACAO")
    private LocalDate dataAtualizacao;

    @OneToMany(mappedBy = "id.moto", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HistoricoPosicao> historicos;

    @OneToMany(mappedBy = "moto", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AlertaEvento> alertas;
}
