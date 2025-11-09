package com.mottu.repository;

import com.mottu.entity.UsuarioSistema;
import com.mottu.entity.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Long> {
    
    // Busca por email (para autenticação)
    Optional<UsuarioSistema> findByEmail(String email);
    
    // Busca por nome
    List<UsuarioSistema> findByNomeContainingIgnoreCase(String nome);
    
    // Busca por perfil
    List<UsuarioSistema> findByPerfil(PerfilUsuario perfil);
    
    // Busca usuários ativos
    List<UsuarioSistema> findByAtivoTrue();
    
    // Busca usuários inativos
    List<UsuarioSistema> findByAtivoFalse();
    
    // Verificar se email já existe (para validação)
    boolean existsByEmail(String email);
    
    // Contar usuários por perfil
    long countByPerfil(PerfilUsuario perfil);
    
    // Query customizada para usuários ativos por perfil
    @Query("SELECT u FROM UsuarioSistema u WHERE u.ativo = true AND u.perfil = :perfil")
    List<UsuarioSistema> findUsuariosAtivosPorPerfil(PerfilUsuario perfil);
}