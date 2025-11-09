package com.mottu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mottu.service.SensorIotService;

@Controller
public class SensorController {

    @Autowired
    private SensorIotService sensorService;

    @GetMapping("/sensores")
    public String listar(Model model) {
        model.addAttribute("sensores", sensorService.listarTodos());
        return "sensor/sensor-list";
    }

    @GetMapping("/sensores/novo")
    public String novo(Model model) {
        model.addAttribute("sensor", new Object());
        return "sensor/sensor-form";
    }
}
