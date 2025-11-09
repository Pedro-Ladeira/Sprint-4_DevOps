package com.mottu.dto;

import com.mottu.entity.PerfilUsuario;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioSistemaDTO {
    
    private Long idUsuario;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 50, message = "Nome deve ter no máximo 50 caracteres")
    private String nome;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;
    
    @Size(min = 6, max = 20, message = "Senha deve ter entre 6 e 20 caracteres")
    private String senha;
    
    @Size(min = 6, max = 20, message = "Confirmação de senha deve ter entre 6 e 20 caracteres")
    private String confirmacaoSenha;
    
    @NotNull(message = "Perfil é obrigatório")
    private PerfilUsuario perfil;
    
    @Builder.Default
    private Boolean ativo = true;
    
    // Validação personalizada para confirmação de senha
    public boolean senhasConferem() {
        return senha != null && senha.equals(confirmacaoSenha);
    }
}