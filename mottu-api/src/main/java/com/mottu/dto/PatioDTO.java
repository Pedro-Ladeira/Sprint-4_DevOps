package com.mottu.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatioDTO {
    
    private Long idPatio;
    
    @NotBlank(message = "Nome do pátio é obrigatório")
    @Size(max = 50, message = "Nome do pátio deve ter no máximo 50 caracteres")
    private String nomePatio;
    
    @NotBlank(message = "Localização é obrigatória")
    @Size(max = 50, message = "Localização deve ter no máximo 50 caracteres")
    private String localizacaoPatio;
    
    @NotNull(message = "Área total é obrigatória")
    @DecimalMin(value = "0.0", inclusive = false, message = "Área total deve ser positiva")
    private BigDecimal areaTotal;
    
    @NotNull(message = "Capacidade máxima é obrigatória")
    @Positive(message = "Capacidade máxima deve ser positiva")
    private Integer capacidadeMaxima;
    
    // Campos calculados para exibição
    private Long quantidadeMotos;
    private Integer espacosDisponiveis;
    private Double taxaOcupacao;
}