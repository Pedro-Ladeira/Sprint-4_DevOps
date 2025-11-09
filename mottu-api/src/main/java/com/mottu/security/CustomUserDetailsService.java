package com.mottu.security;

import com.mottu.entity.UsuarioSistema;
import com.mottu.repository.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("customUserDetailsService")
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioSistemaRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UsuarioSistema> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado com email: " + email);
        }
        
        UsuarioSistema usuario = usuarioOpt.get();
        
        // Verificar se o usuário está ativo
        if (!usuario.getAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo: " + email);
        }
        
        return usuario;
    }
    
    @Transactional(readOnly = true)
    public UsuarioSistema loadUserByEmail(String email) {
        Optional<UsuarioSistema> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado com email: " + email);
        }
        
        return usuarioOpt.get();
    }
    
    @Transactional(readOnly = true)
    public boolean isValidUser(String email, String senha) {
        try {
            UsuarioSistema usuario = loadUserByEmail(email);
            return usuario.getAtivo() && usuario.getSenha().equals(senha);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Transactional(readOnly = true)
    public boolean isUserActive(String email) {
        try {
            UsuarioSistema usuario = loadUserByEmail(email);
            return usuario.getAtivo();
        } catch (Exception e) {
            return false;
        }
    }
}