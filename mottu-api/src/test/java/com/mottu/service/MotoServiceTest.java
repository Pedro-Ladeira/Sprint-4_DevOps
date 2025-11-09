package com.mottu.service;

import com.mottu.entity.Moto;
import com.mottu.repository.AlertaEventoRepository;
import com.mottu.repository.HistoricoPosicaoRepository;
import com.mottu.repository.MotoRepository;
import com.mottu.repository.SensorIotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MotoServiceTest {

    @Mock
    private MotoRepository motoRepository;

    @Mock
    private AlertaEventoRepository alertaEventoRepository;

    @Mock
    private HistoricoPosicaoRepository historicoPosicaoRepository;

    @Mock
    private SensorIotRepository sensorIotRepository;

    @InjectMocks
    private MotoService motoService;

    @Test
    void saveNewMoto_callsSave() {
        Moto moto = new Moto();
        moto.setPlaca("ABC123");

        when(motoRepository.save(moto)).thenReturn(moto);

        Moto saved = motoService.salvar(moto);

        verify(motoRepository, times(1)).save(moto);
        assertEquals("ABC123", saved.getPlaca());
    }

    @Test
    void updateExistingMoto_updatesFieldsAndSaves() {
        Moto original = new Moto();
        original.setIdMoto(1);
        original.setPlaca("OLD");

        when(motoRepository.findById(1)).thenReturn(Optional.of(original));
        when(motoRepository.save(any(Moto.class))).thenAnswer(i -> i.getArgument(0));

        Moto updated = new Moto();
        updated.setIdMoto(1);
        updated.setPlaca("NEW");

        Moto result = motoService.salvar(updated);

        verify(motoRepository, times(1)).findById(1);
        verify(motoRepository, times(1)).save(any(Moto.class));
        assertEquals("NEW", result.getPlaca());
    }
}
