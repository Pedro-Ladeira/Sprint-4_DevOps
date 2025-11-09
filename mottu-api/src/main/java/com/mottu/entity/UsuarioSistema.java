package com.mottu.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "USUARIO_SISTEMA")
@Data
public class UsuarioSistema implements UserDetails {

    @Id
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "NOME", length = 50)
    private String nome;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "SENHA", length = 255)
    private String senha;

    @Column(name = "ATIVO")
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(name = "PERFIL")
    private PerfilUsuario perfil;

    // Métodos do UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() { return senha; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return ativo != null && ativo; }
}
