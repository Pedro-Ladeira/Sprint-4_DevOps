package com.mottu.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeloDTO {
    
    private Long idModelo;
    
    @NotBlank(message = "Fabricante é obrigatório")
    @Size(max = 50, message = "Fabricante deve ter no máximo 50 caracteres")
    private String fabricante;
    
    @NotBlank(message = "Nome do modelo é obrigatório")
    @Size(max = 50, message = "Nome do modelo deve ter no máximo 50 caracteres")
    private String nomeModelo;
    
    @NotNull(message = "Cilindrada é obrigatória")
    @Positive(message = "Cilindrada deve ser positiva")
    private Integer cilindrada;
    
    @NotBlank(message = "Tipo é obrigatório")
    @Size(max = 30, message = "Tipo deve ter no máximo 30 caracteres")
    private String tipo;
    
    // Ano do modelo (adicionado)
    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1900, message = "Ano inválido")
    @Max(value = 2100, message = "Ano inválido")
    private Integer ano;

    // Campo calculado para exibição
    private Long quantidadeMotos;
}