package com.mottu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "HISTORICO_POSICAO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPosicao {

    @EmbeddedId
    private HistoricoPosicaoId id;

    @Column(name = "POSICAO_X")
    private Double posicaoX;

    @Column(name = "POSICAO_Y")
    private Double posicaoY;

    @Column(name = "ACURACIA_LOCALIZACAO")
    private Double acuraciaLocalizacao;

    @Column(name = "ORIGEM_DETECTADA", length = 20)
    private String origemDetectada;

    @Column(name = "STATUS_NO_MOMENTO", length = 20)
    private String statusNoMomento;
}
