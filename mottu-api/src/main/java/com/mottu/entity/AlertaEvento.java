package com.mottu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "ALERTA_EVENTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaEvento {

    @Id
    @Column(name = "ID_ALERTA")
    private Integer idAlerta; // INTEGER no banco

    @Column(name = "TIPO_ALERTA", length = 50)
    private String tipoAlerta;

    @Column(name = "DATA_GERACAO")
    private LocalDate dataGeracao; // DATE no banco

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MOTO", referencedColumnName = "ID_MOTO")
    private Moto moto;
}
