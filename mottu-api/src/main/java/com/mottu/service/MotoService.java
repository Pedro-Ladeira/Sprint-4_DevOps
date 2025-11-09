package com.mottu.service;

import com.mottu.entity.Moto;
import com.mottu.repository.AlertaEventoRepository;
import com.mottu.repository.HistoricoPosicaoRepository;
import com.mottu.repository.MotoRepository;
import com.mottu.repository.SensorIotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MotoService {

    private static final Logger log = LoggerFactory.getLogger(MotoService.class);

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private AlertaEventoRepository alertaEventoRepository;

    @Autowired
    private HistoricoPosicaoRepository historicoPosicaoRepository;

    @Autowired
    private SensorIotRepository sensorIotRepository;

    public List<Moto> listarTodas() {
        return motoRepository.findAll();
    }

    public Optional<Moto> buscarPorId(Integer id) {
        return motoRepository.findById(id);
    }

    @Transactional
    public Moto salvar(Moto moto) {
        if (moto.getIdMoto() != null) {
            // Edição: buscar a moto original do banco
            Moto original = motoRepository.findById(moto.getIdMoto())
                .orElseThrow(() -> new RuntimeException("Moto não encontrada"));
            // Atualizar apenas os campos editáveis
            original.setPlaca(moto.getPlaca());
            original.setChassi(moto.getChassi());
            original.setAnoFabricacao(moto.getAnoFabricacao());
            original.setStatus(moto.getStatus());
            original.setModelo(moto.getModelo());
            original.setPatio(moto.getPatio());
            original.setSensorIot(moto.getSensorIot());
            original.setDataAtualizacao(moto.getDataAtualizacao());
            // NÃO mexer nas listas de alertas/históricos!
            Moto saved = motoRepository.saveAndFlush(original);
            log.debug("Moto atualizada e flush realizada. id={}", saved.getIdMoto());
            return saved;
        } else {
            // Cadastro novo
            Moto saved = motoRepository.saveAndFlush(moto);
            log.debug("Nova moto salva e flush realizada. id={}", saved.getIdMoto());
            // Verifica imediatamente se o registro é encontrável
            Optional<Moto> found = motoRepository.findById(saved.getIdMoto());
            log.debug("FindById após save: present={}", found.isPresent());
            return saved;
        }
    }

    @Transactional
    public void deletar(Integer id) {
        Optional<Moto> motoOpt = motoRepository.findById(id);
        if (motoOpt.isPresent()) {
            Moto moto = motoOpt.get();
            // 1. Zerar o campo id_sensor_iot da moto e salvar
            moto.setSensorIot(null);
            motoRepository.save(moto);
            // 2. Desassociar sensores pelo idMoto
            sensorIotRepository.desassociarMotoById(moto.getIdMoto());
            // 3. Remover todos os históricos relacionados
            historicoPosicaoRepository.deleteByMoto(moto);
            // 4. Remover todos os alertas relacionados
            alertaEventoRepository.deleteByMoto(moto);
            // 5. Excluir a moto
            motoRepository.delete(moto);
        }
    }
}