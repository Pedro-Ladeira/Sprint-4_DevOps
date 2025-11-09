package com.mottu.service;

import com.mottu.entity.AlertaEvento;
import com.mottu.entity.Moto;
import com.mottu.repository.AlertaEventoRepository;
import com.mottu.repository.MotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class AlertaEventoService {

    private static final Logger logger = LoggerFactory.getLogger(AlertaEventoService.class);

    @Autowired
    private AlertaEventoRepository alertaEventoRepository;

    @Autowired
    private MotoRepository motoRepository;

    @Transactional(readOnly = true)
    public List<AlertaEvento> listarTodos() {
        return alertaEventoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<AlertaEvento> listarTodos(Pageable pageable) {
        return alertaEventoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<AlertaEvento> buscarPorId(Long id) {
        return alertaEventoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<AlertaEvento> buscarPorTipoAlerta(String tipoAlerta) {
        return alertaEventoRepository.findByTipoAlerta(tipoAlerta);
    }

    @Transactional(readOnly = true)
    public List<AlertaEvento> buscarPorMoto(Long motoId) {
        Optional<Moto> motoOpt = motoRepository.findById(motoId.intValue());
        return motoOpt.map(moto -> alertaEventoRepository.findByMoto(moto)).orElseGet(java.util.Collections::emptyList);
    }

    @Transactional(readOnly = true)
    public List<AlertaEvento> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return alertaEventoRepository.findByDataGeracaoBetween(inicio, fim);
    }

    @Transactional(readOnly = true)
    public List<AlertaEvento> buscarAlertasRecentes(int dias) {
        LocalDate dataLimite = LocalDate.now().minusDays(dias); // LocalDate, não LocalDateTime
        return alertaEventoRepository.findByDataGeracaoAfter(dataLimite);
    }

    @Transactional(readOnly = true)
    public List<AlertaEvento> buscarAlertasDeHoje() {
        LocalDate hoje = LocalDate.now(); // LocalDate, não LocalDateTime
        return buscarPorPeriodo(hoje, hoje);
    }

    @Transactional(readOnly = true)
    public long contarAlertasPorTipo(String tipoAlerta) {
        return alertaEventoRepository.countByTipoAlerta(tipoAlerta);
    }

    @Transactional(readOnly = true)
    public long contarAlertasPorMoto(Long motoId) {
        Optional<Moto> motoOpt = motoRepository.findById(motoId.intValue());
        return motoOpt.map(moto -> alertaEventoRepository.countByMoto(moto)).orElse(0L);
    }

    public AlertaEvento salvar(AlertaEvento alerta) {
        validarAlerta(alerta);
        return alertaEventoRepository.save(alerta);
    }

    public AlertaEvento criarAlerta(String tipoAlerta, Long motoId) {
        Moto moto = motoRepository.findById(motoId.intValue())
                .orElseThrow(() -> new RuntimeException("Moto não encontrada com ID: " + motoId));
        AlertaEvento alerta = AlertaEvento.builder()
                .tipoAlerta(tipoAlerta)
                .dataGeracao(LocalDate.now()) // LocalDate, não LocalDateTime
                .moto(moto)
                .build();

        return salvar(alerta);
    }

    public void processarAlertas() {
        List<AlertaEvento> alertasRecentes = buscarAlertasRecentes(1);

        alertasRecentes.stream()
                .collect(java.util.stream.Collectors.groupingBy(AlertaEvento::getTipoAlerta))
                .forEach((tipo, alertas) -> {
                    logger.info("Tipo: {} - Quantidade: {}", tipo, alertas.size());
                });
    }

    public void deletar(Long id) {
        if (!alertaEventoRepository.existsById(id)) {
            throw new RuntimeException("Alerta não encontrado com ID: " + id);
        }
        alertaEventoRepository.deleteById(id);
    }

    public void deletarAlertasAntigos(int diasParaManterAlertas) {
        LocalDate dataLimite = LocalDate.now().minusDays(diasParaManterAlertas); // LocalDate, não LocalDateTime
        alertaEventoRepository.deleteByDataGeracaoBefore(dataLimite);
    }

    private void validarAlerta(AlertaEvento alerta) {
        if (alerta.getTipoAlerta() == null || alerta.getTipoAlerta().trim().isEmpty()) {
            throw new RuntimeException("Tipo de alerta é obrigatório");
        }

        if (alerta.getMoto() == null || alerta.getMoto().getIdMoto() == null) {
            throw new RuntimeException("Moto é obrigatória para o alerta");
        }

        if (alerta.getDataGeracao() == null) {
            alerta.setDataGeracao(LocalDate.now()); // LocalDate, não LocalDateTime
        }
    }
}
