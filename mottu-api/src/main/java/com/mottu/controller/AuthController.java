package com.mottu.controller;

import com.mottu.dto.LoginDTO;
import com.mottu.entity.UsuarioSistema;
import com.mottu.service.UsuarioSistemaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioSistemaService usuarioService;

        @Autowired
        private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Email ou senha inválidos!");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Logout realizado com sucesso!");
        }

        model.addAttribute("loginDTO", new LoginDTO());

        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute LoginDTO loginDTO,
                               BindingResult result,
                               HttpServletRequest request,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "auth/login";
        }

        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha());

            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (loginDTO.getLembrarMe() != null && loginDTO.getLembrarMe()) {
                request.getSession().setMaxInactiveInterval(30 * 24 * 60 * 60);
            }

            redirectAttributes.addFlashAttribute("success", "Login realizado com sucesso!");
            return "redirect:/dashboard";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Email ou senha inválidos!");
            model.addAttribute("loginDTO", loginDTO);
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response,
                         RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        redirectAttributes.addFlashAttribute("logoutMessage", "Logout realizado com sucesso!");
        return "redirect:/auth/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
            model.addAttribute("message", "Você não tem permissão para acessar esta página.");
        } else {
            model.addAttribute("message", "Você precisa fazer login para acessar esta página.");
        }
        return "auth/access-denied";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        if (principal != null) {
            UsuarioSistema usuario = (UsuarioSistema) usuarioService.loadUserByUsername(principal.getName());
            model.addAttribute("usuario", usuario);
            return "auth/profile";
        }
        return "redirect:/auth/login";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String senhaAtual,
                                 @RequestParam String novaSenha,
                                 @RequestParam String confirmaSenha,
                                 Principal principal,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/auth/login";
        }

        if (!novaSenha.equals(confirmaSenha)) {
            model.addAttribute("error", "Nova senha e confirmação não coincidem!");
            return "auth/profile";
        }

        try {
            UsuarioSistema usuario = (UsuarioSistema) usuarioService.loadUserByUsername(principal.getName());
            usuarioService.alterarSenha(usuario.getIdUsuario().longValue(), novaSenha);

            redirectAttributes.addFlashAttribute("success", "Senha alterada com sucesso!");
            return "redirect:/auth/profile";

        } catch (Exception e) {
            model.addAttribute("error", "Erro ao alterar senha: " + e.getMessage());
            return "auth/profile";
        }
    }

    @ResponseBody
    @PostMapping("/api/login")
    public java.util.Map<String, Object> apiLogin(@RequestBody LoginDTO loginDTO) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();

        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha());

            Authentication authentication = authenticationManager.authenticate(authToken);

            if (authentication.isAuthenticated()) {
                response.put("success", true);
                response.put("message", "Login realizado com sucesso!");
                response.put("user", authentication.getName());
                response.put("authorities", authentication.getAuthorities());
            } else {
                response.put("success", false);
                response.put("message", "Credenciais inválidas!");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro na autenticação: " + e.getMessage());
        }

        return response;
    }

    @ResponseBody
    @PostMapping("/api/logout")
    public java.util.Map<String, String> apiLogout(HttpServletRequest request,
                                                   HttpServletResponse response) {
        java.util.Map<String, String> responseMap = new java.util.HashMap<>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                responseMap.put("success", "true");
                responseMap.put("message", "Logout realizado com sucesso!");
            } else {
                responseMap.put("success", "false");
                responseMap.put("message", "Usuário não autenticado!");
            }

        } catch (Exception e) {
            responseMap.put("success", "false");
            responseMap.put("message", "Erro no logout: " + e.getMessage());
        }

        return responseMap;
    }

    @ResponseBody
    @GetMapping("/api/user-info")
    public java.util.Map<String, Object> userInfo(Principal principal) {
        java.util.Map<String, Object> userInfo = new java.util.HashMap<>();

        if (principal != null) {
            UsuarioSistema usuario = (UsuarioSistema) usuarioService.loadUserByUsername(principal.getName());
            userInfo.put("authenticated", true);
            userInfo.put("username", usuario.getNome());
            userInfo.put("email", usuario.getEmail());
            userInfo.put("perfil", usuario.getPerfil());
            userInfo.put("authorities", usuario.getAuthorities());
        } else {
            userInfo.put("authenticated", false);
            userInfo.put("message", "Usuário não autenticado");
        }

        return userInfo;
    }
}
