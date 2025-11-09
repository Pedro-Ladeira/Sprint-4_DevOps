package com.mottu.service;

import com.mottu.entity.HistoricoPosicao;
import com.mottu.entity.Moto;
import com.mottu.entity.HistoricoPosicaoId;
import com.mottu.repository.HistoricoPosicaoRepository;
import com.mottu.repository.MotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistoricoPosicaoService {

    @Autowired
    private HistoricoPosicaoRepository historicoRepository;

    @Autowired
    private MotoRepository motoRepository;

    @Transactional(readOnly = true)
    public List<HistoricoPosicao> listarTodos() {
        return historicoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<HistoricoPosicao> listarTodos(Pageable pageable) {
        return historicoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<HistoricoPosicao> buscarPorMoto(Long motoId) {
        // Ajuste: buscar por id.moto.idMoto
        return historicoRepository.findAll().stream()
            .filter(h -> h.getId() != null && h.getId().getMoto() != null && h.getId().getMoto().getIdMoto().equals(motoId.intValue()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<HistoricoPosicao> buscarPorMotoOrderByData(Long motoId) {
        return historicoRepository.findAll().stream()
            .filter(h -> h.getId() != null && h.getId().getMoto() != null && h.getId().getMoto().getIdMoto().equals(motoId.intValue()))
            .sorted((a, b) -> b.getId().getDataAtualizacao().compareTo(a.getId().getDataAtualizacao()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<HistoricoPosicao> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return historicoRepository.findAll().stream()
            .filter(h -> h.getId() != null &&
                         h.getId().getDataAtualizacao() != null &&
                         (h.getId().getDataAtualizacao().isEqual(inicio) || h.getId().getDataAtualizacao().isAfter(inicio)) &&
                         (h.getId().getDataAtualizacao().isEqual(fim) || h.getId().getDataAtualizacao().isBefore(fim)))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<HistoricoPosicao> buscarPorMotoEPeriodo(Long motoId, LocalDate inicio, LocalDate fim) {
        return historicoRepository.findAll().stream()
            .filter(h -> h.getId() != null &&
                         h.getId().getMoto() != null &&
                         h.getId().getMoto().getIdMoto().equals(motoId.intValue()) &&
                         h.getId().getDataAtualizacao() != null &&
                         (h.getId().getDataAtualizacao().isEqual(inicio) || h.getId().getDataAtualizacao().isAfter(inicio)) &&
                         (h.getId().getDataAtualizacao().isEqual(fim) || h.getId().getDataAtualizacao().isBefore(fim)))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<HistoricoPosicao> buscarPorOrigemDetectada(String origem) {
        return historicoRepository.findAll().stream()
            .filter(h -> origem.equals(h.getOrigemDetectada()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<HistoricoPosicao> buscarPorStatusNoMomento(String status) {
        return historicoRepository.findAll().stream()
            .filter(h -> status.equals(h.getStatusNoMomento()))
            .toList();
    }

    @Transactional(readOnly = true)
    public Optional<HistoricoPosicao> buscarUltimaPosicaoMoto(Long motoId) {
        return buscarPorMotoOrderByData(motoId).stream().findFirst();
    }

    @Transactional(readOnly = true)
    public List<HistoricoPosicao> buscarHistoricoRecente(Long motoId, int dias) {
        LocalDate dataLimite = LocalDate.now().minusDays(dias);
        return historicoRepository.findAll().stream()
            .filter(h -> h.getId() != null &&
                         h.getId().getMoto() != null &&
                         h.getId().getMoto().getIdMoto().equals(motoId.intValue()) &&
                         h.getId().getDataAtualizacao() != null &&
                         h.getId().getDataAtualizacao().isAfter(dataLimite))
            .toList();
    }

    @Transactional(readOnly = true)
    public long contarRegistrosPorMoto(Long motoId) {
        return historicoRepository.findAll().stream()
            .filter(h -> h.getId() != null &&
                         h.getId().getMoto() != null &&
                         h.getId().getMoto().getIdMoto().equals(motoId.intValue()))
            .count();
    }

    public HistoricoPosicao salvar(HistoricoPosicao historico) {
        validarHistorico(historico);
        return historicoRepository.save(historico);
    }

    public HistoricoPosicao registrarPosicao(Long motoId, BigDecimal posX, BigDecimal posY,
                                             BigDecimal acuracia, String origem, String status) {
        Moto moto = motoRepository.findById(motoId.intValue())
                .orElseThrow(() -> new RuntimeException("Moto não encontrada com ID: " + motoId));
        HistoricoPosicaoId id = new HistoricoPosicaoId(LocalDate.now(), moto);
        HistoricoPosicao historico = HistoricoPosicao.builder()
                .id(id)
                .posicaoX(posX.doubleValue())
                .posicaoY(posY.doubleValue())
                .acuraciaLocalizacao(acuracia.doubleValue())
                .origemDetectada(origem)
                .statusNoMomento(status)
                .build();
        return salvar(historico);
    }

    public void limparHistoricoAntigo(int diasParaManterHistorico) {
        LocalDate dataLimite = LocalDate.now().minusDays(diasParaManterHistorico);
        historicoRepository.findAll().stream()
            .filter(h -> h.getId() != null &&
                         h.getId().getDataAtualizacao() != null &&
                         h.getId().getDataAtualizacao().isBefore(dataLimite))
            .forEach(historicoRepository::delete);
    }

    public void deletar(Long motoId, LocalDate dataAtualizacao) {
        Moto moto = motoRepository.findById(motoId.intValue())
                .orElseThrow(() -> new RuntimeException("Moto não encontrada com ID: " + motoId));
        HistoricoPosicaoId id = new HistoricoPosicaoId(dataAtualizacao, moto);
        historicoRepository.deleteById(id);
    }

    private void validarHistorico(HistoricoPosicao historico) {
        if (historico.getId() == null || historico.getId().getMoto() == null || historico.getId().getMoto().getIdMoto() == null) {
            throw new RuntimeException("Moto é obrigatória");
        }
        if (historico.getPosicaoX() == null || historico.getPosicaoY() == null) {
            throw new RuntimeException("Posições X e Y são obrigatórias");
        }
        if (historico.getAcuraciaLocalizacao() == null || historico.getAcuraciaLocalizacao() < 0) {
            throw new RuntimeException("Acurácia deve ser um valor positivo");
        }
        if (historico.getOrigemDetectada() == null || historico.getOrigemDetectada().trim().isEmpty()) {
            throw new RuntimeException("Origem detectada é obrigatória");
        }
        if (historico.getStatusNoMomento() == null || historico.getStatusNoMomento().trim().isEmpty()) {
            throw new RuntimeException("Status no momento é obrigatório");
        }
        if (historico.getId().getDataAtualizacao() == null) {
            historico.getId().setDataAtualizacao(LocalDate.now());
        }
    }
}
