package com.mottu.security;

import com.mottu.entity.PerfilUsuario;
import com.mottu.entity.UsuarioSistema;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

public class SecurityUtils {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Obtém o usuário atualmente autenticado
     */
    public static UsuarioSistema getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UsuarioSistema) {
            return (UsuarioSistema) authentication.getPrincipal();
        }
        
        return null;
    }

    /**
     * Obtém o email do usuário atualmente autenticado
     */
    public static String getCurrentUserEmail() {
        UsuarioSistema usuario = getCurrentUser();
        return usuario != null ? usuario.getEmail() : null;
    }

    /**
     * Obtém o nome do usuário atualmente autenticado
     */
    public static String getCurrentUserName() {
        UsuarioSistema usuario = getCurrentUser();
        return usuario != null ? usuario.getNome() : null;
    }

    /**
     * Obtém o perfil do usuário atualmente autenticado
     */
    public static PerfilUsuario getCurrentUserPerfil() {
        UsuarioSistema usuario = getCurrentUser();
        return usuario != null ? usuario.getPerfil() : null;
    }

    /**
     * Verifica se o usuário atual tem um perfil específico
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            return false;
        }
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_" + role)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Verifica se o usuário atual é ADMIN
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Verifica se o usuário atual é OPERADOR
     */
    public static boolean isOperador() {
        return hasRole("OPERADOR");
    }

    /**
     * Verifica se o usuário atual é MANUTENCAO
     */
    public static boolean isManutencao() {
        return hasRole("MANUTENCAO");
    }

    /**
     * Verifica se o usuário atual tem permissão para gerenciar motos
     */
    public static boolean canManageMotos() {
        return isAdmin() || isOperador();
    }

    /**
     * Verifica se o usuário atual tem permissão para gerenciar usuários
     */
    public static boolean canManageUsers() {
        return isAdmin();
    }

    /**
     * Verifica se o usuário atual tem permissão para gerenciar modelos e pátios
     */
    public static boolean canManageModelosPatios() {
        return isAdmin();
    }

    /**
     * Verifica se o usuário atual tem permissão para ver relatórios
     */
    public static boolean canViewReports() {
        return isAdmin() || isOperador();
    }

    /**
     * Verifica se há um usuário autenticado
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        return authentication != null && 
               authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * Criptografa uma senha
     */
    public static String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verifica se uma senha corresponde à senha criptografada
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Gera uma senha aleatória
     */
    public static String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }

    /**
     * Valida se uma senha atende aos critérios mínimos de segurança
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        // Pelo menos uma letra maiúscula, uma minúscula e um número
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        return hasUpper && hasLower && hasDigit;
    }

    /**
     * Obtém uma descrição textual dos perfis do usuário atual
     */
    public static String getCurrentUserRolesDescription() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            return "Não autenticado";
        }
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        StringBuilder roles = new StringBuilder();
        
        for (GrantedAuthority authority : authorities) {
            if (roles.length() > 0) {
                roles.append(", ");
            }
            roles.append(authority.getAuthority().replace("ROLE_", ""));
        }
        
        return roles.toString();
    }

    /**
     * Verifica se o usuário atual pode acessar um recurso baseado no perfil
     */
    public static boolean canAccess(String... requiredRoles) {
        if (requiredRoles == null || requiredRoles.length == 0) {
            return isAuthenticated();
        }
        
        for (String role : requiredRoles) {
            if (hasRole(role)) {
                return true;
            }
        }
        
        return false;
    }
}