package com.mottu.service;

import com.mottu.entity.SensorIot;
import com.mottu.repository.SensorIotRepository;
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
public class SensorIotService {

    @Autowired
    private SensorIotRepository sensorIotRepository;

    @Transactional(readOnly = true)
    public List<SensorIot> listarTodos() {
        return sensorIotRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<SensorIot> listarTodos(Pageable pageable) {
        return sensorIotRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<SensorIot> buscarPorId(Long id) {
        return sensorIotRepository.findById(id.intValue()); // Converte Long->Integer
    }

    @Transactional(readOnly = true)
    public List<SensorIot> buscarPorTipo(String tipo) {
        return sensorIotRepository.findByTipoSensor(tipo);
    }

    @Transactional(readOnly = true)
    public List<SensorIot> buscarPorDataTransmissao(LocalDate data) {
        return sensorIotRepository.findByDataTransmissao(data);
    }

    @Transactional(readOnly = true)
    public List<SensorIot> buscarPorFaixaBateria(BigDecimal min, BigDecimal max) {
        return sensorIotRepository.findByBateriaPercentualBetween(min, max);
    }

    @Transactional(readOnly = true)
    public List<SensorIot> buscarSensoresComBateriaBaixa(BigDecimal limite) {
        return sensorIotRepository.findByBateriaPercentualLessThan(limite);
    }

    @Transactional(readOnly = true)
    public List<SensorIot> buscarSensoresSemMoto() {
        return sensorIotRepository.findSensoresSemMoto();
    }

    @Transactional(readOnly = true)
    public List<SensorIot> buscarSensoresAtivos() {
        LocalDate dataLimite = LocalDate.now().minusDays(7);
        return sensorIotRepository.findByDataTransmissaoAfter(dataLimite);
    }

    public SensorIot salvar(SensorIot sensor) {
        validarSensor(sensor);
        return sensorIotRepository.save(sensor);
    }

    public SensorIot atualizarBateria(Long sensorId, BigDecimal novoPercentual) {
        Optional<SensorIot> sensorOpt = buscarPorId(sensorId);
        if (sensorOpt.isPresent()) {
            SensorIot sensor = sensorOpt.get();
            sensor.setBateriaPercentual(novoPercentual.doubleValue()); // Converte BigDecimal->Double
            sensor.setDataTransmissao(LocalDate.now());
            return sensorIotRepository.save(sensor);
        }
        throw new RuntimeException("Sensor não encontrado com ID: " + sensorId);
    }

    public SensorIot atualizarTransmissao(Long sensorId) {
        Optional<SensorIot> sensorOpt = buscarPorId(sensorId);
        if (sensorOpt.isPresent()) {
            SensorIot sensor = sensorOpt.get();
            sensor.setDataTransmissao(LocalDate.now());
            return sensorIotRepository.save(sensor);
        }
        throw new RuntimeException("Sensor não encontrado com ID: " + sensorId);
    }

    public void deletar(Long id) {
        if (!sensorIotRepository.existsById(id.intValue())) {
            throw new RuntimeException("Sensor não encontrado com ID: " + id);
        }
        sensorIotRepository.deleteById(id.intValue());
    }

    private void validarSensor(SensorIot sensor) {
        if (sensor.getTipoSensor() == null || sensor.getTipoSensor().trim().isEmpty()) {
            throw new RuntimeException("Tipo do sensor é obrigatório");
        }

        if (sensor.getBateriaPercentual() == null ||
                sensor.getBateriaPercentual() < 0 ||
                sensor.getBateriaPercentual() > 100) {
            throw new RuntimeException("Percentual da bateria deve estar entre 0 e 100");
        }

        if (sensor.getDataTransmissao() == null) {
            sensor.setDataTransmissao(LocalDate.now());
        }
    }
}
