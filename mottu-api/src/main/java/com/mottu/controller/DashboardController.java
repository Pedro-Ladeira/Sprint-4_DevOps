package com.mottu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalMotos", 0);
        model.addAttribute("motosDisponiveis", 0);
        model.addAttribute("motosAlugadas", 0);
        model.addAttribute("motosManutencao", 0);
        model.addAttribute("totalPatios", 0);
        model.addAttribute("totalModelos", 0);
        model.addAttribute("alertasHoje", Collections.emptyList());
        model.addAttribute("alertasRecentes", Collections.emptyList());
        model.addAttribute("patiosDisponiveis", Collections.emptyList());
        return "dashboard/index";
    }
}
