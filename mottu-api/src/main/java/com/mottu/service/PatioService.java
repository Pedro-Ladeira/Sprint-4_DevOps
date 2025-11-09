package com.mottu.service;

import com.mottu.entity.Patio;
import com.mottu.repository.PatioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatioService {

    @Autowired
    private PatioRepository patioRepository;

    @Transactional(readOnly = true)
    public List<Patio> listarTodos() {
        return patioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Patio> listarTodos(Pageable pageable) {
        return patioRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Patio> buscarPorId(Long id) {
        return patioRepository.findById(id.intValue()); // Converte Long->Integer
    }

    @Transactional(readOnly = true)
    public Optional<Patio> buscarPorNome(String nomePatio) {
        return patioRepository.findByNomePatio(nomePatio);
    }

    @Transactional(readOnly = true)
    public List<Patio> buscarPorLocalizacao(String localizacao) {
        return patioRepository.findByLocalizacaoPatio(localizacao);
    }

    @Transactional(readOnly = true)
    public List<Patio> buscarPorLocalizacaoContendo(String localizacao) {
        return patioRepository.findByLocalizacaoPatioContainingIgnoreCase(localizacao);
    }

    @Transactional(readOnly = true)
    public List<Patio> buscarPorCapacidadeMinima(Integer capacidadeMinima) {
        return patioRepository.findByCapacidadeMaximaGreaterThanEqual(capacidadeMinima);
    }

    @Transactional(readOnly = true)
    public List<Patio> buscarPorFaixaArea(BigDecimal areaMin, BigDecimal areaMax) {
        return patioRepository.findByAreaTotalBetween(areaMin, areaMax);
    }

    @Transactional(readOnly = true)
    public List<Patio> buscarPatiosComEspacoDisponivel() {
        return patioRepository.findPatiosComEspacoDisponivel();
    }

    @Transactional(readOnly = true)
    public long contarMotosPorPatio(Long patioId) {
        return patioRepository.countMotosByPatioId(patioId.intValue()); // Converte Long->Integer
    }

    @Transactional(readOnly = true)
    public Double calcularTaxaOcupacao(Long patioId) {
        BigDecimal taxa = patioRepository.calcularTaxaOcupacao(patioId.intValue()); // Repository retorna BigDecimal
        return taxa != null ? taxa.doubleValue() : 0.0; // Converte BigDecimal->Double
    }

    @Transactional(readOnly = true)
    public Integer calcularEspacosDisponiveis(Long patioId) {
        Optional<Patio> patio = buscarPorId(patioId);
        if (patio.isPresent()) {
            long motosOcupadas = contarMotosPorPatio(patioId);
            return (int) (patio.get().getCapacidadeMaxima() - motosOcupadas);
        }
        return 0;
    }

    public Patio salvar(Patio patio) {
        validarPatio(patio);
        return patioRepository.save(patio);
    }

    public void deletar(Long id) {
        if (!patioRepository.existsById(id.intValue())) {
            throw new RuntimeException("Pátio não encontrado com ID: " + id);
        }

        long motosNoPatio = contarMotosPorPatio(id);
        if (motosNoPatio > 0) {
            throw new RuntimeException("Não é possível deletar o pátio. Existem " + motosNoPatio + " motos no pátio.");
        }

        patioRepository.deleteById(id.intValue());
    }

    private void validarPatio(Patio patio) {
        if (patio.getIdPatio() == null || !patio.getNomePatio().equals(buscarPorId(patio.getIdPatio().longValue()).map(Patio::getNomePatio).orElse(null))) {
            if (buscarPorNome(patio.getNomePatio()).isPresent()) {
                throw new RuntimeException("Já existe um pátio com este nome: " + patio.getNomePatio());
            }
        }

        if (patio.getCapacidadeMaxima() <= 0) {
            throw new RuntimeException("Capacidade máxima deve ser um valor positivo");
        }

        if (patio.getAreaTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Área total deve ser um valor positivo");
        }
    }
}
