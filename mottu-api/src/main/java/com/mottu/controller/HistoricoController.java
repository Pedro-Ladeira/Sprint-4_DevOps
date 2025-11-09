package com.mottu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mottu.service.HistoricoPosicaoService;

@Controller
public class HistoricoController {

    @Autowired
    private HistoricoPosicaoService historicoService;

    @GetMapping("/historico")
    public String listar(Model model) {
        model.addAttribute("registros", historicoService.listarTodos());
        return "historico/historico-list";
    }
}
