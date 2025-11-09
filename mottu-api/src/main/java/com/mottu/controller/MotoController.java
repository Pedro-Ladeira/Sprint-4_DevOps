package com.mottu.controller;

import com.mottu.entity.Moto;
import com.mottu.service.MotoService;
import com.mottu.service.PatioService;
import com.mottu.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@Controller
@RequestMapping("/motos")
public class MotoController {

    @Autowired
    private MotoService motoService;

    @Autowired
    private PatioService patioService;

    @Autowired
    private ModeloService modeloService;

    @GetMapping
    public String listar(@RequestParam(value = "placa", required = false, defaultValue = "") String placa,
                        @RequestParam(value = "status", required = false, defaultValue = "") String status,
                        @RequestParam(value = "patioId", required = false, defaultValue = "") String patioId,
                        Model model) {
        model.addAttribute("motos", motoService.listarTodas());
        model.addAttribute("patios", patioService.listarTodos());
        model.addAttribute("placa", placa);
        model.addAttribute("status", status);
        model.addAttribute("patioId", patioId);
        return "moto/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("patios", patioService.listarTodos());
        model.addAttribute("modelos", modeloService.listarTodos());
        return "moto/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Moto moto, BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("patios", patioService.listarTodos());
            model.addAttribute("modelos", modeloService.listarTodos());
            return "moto/form";
        }
        if (moto.getModelo() != null && moto.getModelo().getIdModelo() != null) {
            moto.setModelo(modeloService.buscarPorId(moto.getModelo().getIdModelo()).orElse(null));
        }
        if (moto.getPatio() != null && moto.getPatio().getIdPatio() != null) {
            moto.setPatio(patioService.buscarPorId(moto.getPatio().getIdPatio()).orElse(null));
        }
        motoService.salvar(moto);
        redirect.addFlashAttribute("success", "Moto salva com sucesso!");
        return "redirect:/motos";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
        Optional<Moto> moto = motoService.buscarPorId(id);
        if (moto.isPresent()) {
            model.addAttribute("moto", moto.get());
            return "moto/view";
        } else {
            redirect.addFlashAttribute("error", "Moto não encontrada!");
            return "redirect:/motos";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
        Optional<Moto> moto = motoService.buscarPorId(id);
        if (moto.isPresent()) {
            model.addAttribute("moto", moto.get());
            model.addAttribute("patios", patioService.listarTodos());
            model.addAttribute("modelos", modeloService.listarTodos());
            return "moto/form";
        } else {
            redirect.addFlashAttribute("error", "Moto não encontrada!");
            return "redirect:/motos";
        }
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Integer id, RedirectAttributes redirect) {
        try {
            motoService.deletar(id);
            redirect.addFlashAttribute("success", "Moto deletada com sucesso!");
        } catch (EmptyResultDataAccessException e) {
            redirect.addFlashAttribute("error", "Moto não encontrada para deleção.");
        } catch (ObjectOptimisticLockingFailureException e) {
            redirect.addFlashAttribute("error", "Falha de concorrência: a moto foi alterada ou removida por outro usuário.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Erro ao deletar moto: " + e.getMessage());
        }
        return "redirect:/motos";
    }
}
