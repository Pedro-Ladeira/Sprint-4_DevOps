package com.mottu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mottu.service.UsuarioSistemaService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioSistemaService usuarioService;

    @GetMapping("/usuarios")
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuario/usuario-list";
    }

    @GetMapping("/usuarios/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Object());
        return "usuario/usuario-form";
    }

    @GetMapping("/usuarios/{id}")
    public String detalhe(@PathVariable Integer id, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorId(id).orElse(null));
        return "usuario/usuario-view";
    }
}
