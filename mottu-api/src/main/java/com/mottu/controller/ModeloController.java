package com.mottu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mottu.entity.Modelo;
import com.mottu.service.ModeloService;

import jakarta.validation.Valid;

@Controller
public class ModeloController {

    @Autowired
    private ModeloService modeloService;

    @GetMapping("/modelos")
    public String listar(Model model) {
        model.addAttribute("modelos", modeloService.listarTodos());
        return "modelo/modelo-list";
    }

    @GetMapping("/modelos/novo")
    public String novo(Model model) {
        model.addAttribute("modelo", new Modelo());
        return "modelo/modelo-form";
    }

    // Test endpoint: página mínima para isolar renderização do formulário
    @GetMapping("/modelos/testnovo")
    public String novoTeste(Model model) {
        model.addAttribute("modelo", new Modelo());
        return "modelo/test-form";
    }

    @GetMapping("/modelos/{id}")
    public String detalhe(@PathVariable Integer id, Model model) {
        model.addAttribute("modelo", modeloService.buscarPorId(id).orElse(new Modelo()));
        return "modelo/modelo-view";
    }

    @PostMapping("/modelos")
    public String criar(@Valid @ModelAttribute("modelo") Modelo modelo, BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("modelo", modelo);
            return "modelo/modelo-form";
        }
        try {
            modeloService.salvar(modelo);
            redirect.addFlashAttribute("success", "Modelo cadastrado com sucesso!");
            return "redirect:/modelos";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao salvar modelo: " + e.getMessage());
            return "modelo/modelo-form";
        }
    }

    @PostMapping("/modelos/{id}")
    public String atualizar(@PathVariable Integer id, @Valid @ModelAttribute("modelo") Modelo modelo, BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("modelo", modelo);
            return "modelo/modelo-form";
        }
        try {
            modelo.setIdModelo(id);
            modeloService.salvar(modelo);
            redirect.addFlashAttribute("success", "Modelo atualizado com sucesso!");
            return "redirect:/modelos";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao atualizar modelo: " + e.getMessage());
            return "modelo/modelo-form";
        }
    }
}
