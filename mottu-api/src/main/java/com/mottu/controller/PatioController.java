package com.mottu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mottu.service.PatioService;
import com.mottu.entity.Patio;

import jakarta.validation.Valid;

@Controller
public class PatioController {

    @Autowired
    private PatioService patioService;

    @GetMapping("/patios")
    public String listar(Model model) {
        model.addAttribute("patios", patioService.listarTodos());
        return "patio/patio-list";
    }

    @GetMapping("/patios/novo")
    public String novo(Model model) {
        model.addAttribute("patio", new Patio());
        return "patio/patio-form";
    }

    @GetMapping("/patios/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        model.addAttribute("patio", patioService.buscarPorId(id).orElse(null));
        return "patio/patio-view";
    }

    @PostMapping("/patios")
    public String criar(@Valid @ModelAttribute("patio") Patio patio, BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            // return to form with validation errors
            model.addAttribute("patio", patio);
            return "patio/patio-form";
        }
        try {
            patioService.salvar(patio);
            redirect.addFlashAttribute("success", "Pátio cadastrado com sucesso!");
            return "redirect:/patios";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao salvar pátio: " + e.getMessage());
            return "patio/patio-form";
        }
    }

    @PostMapping("/patios/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("patio") Patio patio, BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("patio", patio);
            return "patio/patio-form";
        }
        try {
            // Ensure id is set
            patio.setIdPatio(id.intValue());
            patioService.salvar(patio);
            redirect.addFlashAttribute("success", "Pátio atualizado com sucesso!");
            return "redirect:/patios";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao atualizar pátio: " + e.getMessage());
            return "patio/patio-form";
        }
    }
}
