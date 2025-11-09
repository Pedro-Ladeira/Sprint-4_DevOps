package com.mottu.service;

import com.mottu.entity.UsuarioSistema;
import com.mottu.repository.UsuarioSistemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioSistemaService {

    @Autowired
    private UsuarioSistemaRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioSistema> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<UsuarioSistema> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioSistema> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioSistema> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioSistema salvar(UsuarioSistema usuario) {
        return usuarioRepository.save(usuario);
    }

    public UsuarioSistema loadUserByUsername(String username) {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + username));
    }

    public void alterarSenha(long id, String novaSenha) {
        Optional<UsuarioSistema> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            UsuarioSistema usuario = usuarioOpt.get();
            usuario.setSenha(novaSenha); // Certifique-se de que a senha já está criptografada se necessário
            usuarioRepository.save(usuario);
        } else {
            throw new RuntimeException("Usuário não encontrado para alteração de senha: " + id);
        }
    }
}
