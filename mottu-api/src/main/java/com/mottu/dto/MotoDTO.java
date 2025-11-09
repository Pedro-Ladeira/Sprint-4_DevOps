package com.mottu.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MotoDTO {
    
    private Long idMoto;
    
    @NotBlank(message = "Placa é obrigatória")
    @Size(max = 10, message = "Placa deve ter no máximo 10 caracteres")
    @Pattern(regexp = "^[A-Z]{3}[0-9][0-9A-Z][0-9]{2}$", message = "Placa deve seguir o padrão brasileiro")
    private String placa;
    
    @NotBlank(message = "Chassi é obrigatório")
    @Size(max = 50, message = "Chassi deve ter no máximo 50 caracteres")
    private String chassi;
    
    @NotNull(message = "Ano de fabricação é obrigatório")
    @Min(value = 1990, message = "Ano de fabricação deve ser maior que 1990")
    @Max(value = 2030, message = "Ano de fabricação não pode ser futuro")
    private Integer anoFabricacao;
    
    @NotBlank(message = "Status é obrigatório")
    @Size(max = 20, message = "Status deve ter no máximo 20 caracteres")
    private String status;
    
    @NotNull(message = "Modelo é obrigatório")
    private Long modeloId;
    private String modeloNome;
    private String modeloFabricante;
    
    @NotNull(message = "Pátio é obrigatório")
    private Long patioId;
    private String patioNome;
    private String patioLocalizacao;
    
    private Long sensorIotId;
    private String sensorTipo;
    
    private LocalDate dataAtualizacao;
}