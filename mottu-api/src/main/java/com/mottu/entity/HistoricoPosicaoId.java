package com.mottu.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class HistoricoPosicaoId implements Serializable {
    @Column(name = "DATA_ATUALIZACAO")
    private LocalDate dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MOTO", referencedColumnName = "ID_MOTO")
    private Moto moto;

    public HistoricoPosicaoId() {}

    public HistoricoPosicaoId(LocalDate dataAtualizacao, Moto moto) {
        this.dataAtualizacao = dataAtualizacao;
        this.moto = moto;
    }

    public LocalDate getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDate dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    public Moto getMoto() { return moto; }
    public void setMoto(Moto moto) { this.moto = moto; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoPosicaoId that = (HistoricoPosicaoId) o;
        return Objects.equals(dataAtualizacao, that.dataAtualizacao) &&
                Objects.equals(moto, that.moto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataAtualizacao, moto);
    }
}
