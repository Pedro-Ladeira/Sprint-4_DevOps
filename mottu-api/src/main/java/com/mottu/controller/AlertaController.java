package com.mottu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mottu.service.AlertaEventoService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AlertaController {

    @Autowired
    private AlertaEventoService alertaService;

    @GetMapping("/alertas")
    public String listar(Model model) {
        model.addAttribute("alertas", alertaService.listarTodos());
        return "alerta/alerta-list";
    }
}
