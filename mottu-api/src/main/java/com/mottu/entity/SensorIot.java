package com.mottu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "SENSOR_IOT")
@Data
public class SensorIot {

    @Id
    @Column(name = "ID_SENSOR_IOT")
    private Integer idSensorIot;

    @Column(name = "TIPO_SENSOR", length = 20)
    private String tipoSensor;

    @Column(name = "DATA_TRANSMISSAO")
    private LocalDate dataTransmissao;

    @Column(name = "BATERIA_PERCENTUAL") // REMOVIDO: precision/scale se tiver
    private Double bateriaPercentual;

    @Column(name = "ID_MOTO")
    private Integer idMoto;
}
