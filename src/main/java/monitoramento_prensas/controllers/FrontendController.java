package monitoramento_prensas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping("/")
    public String home() {
        return "dashboard.html";
    }

    @GetMapping("/historico-falhas/list")
    public String historicoFalhasList() {
        return "historico-falhas/list.html";
    }

    @GetMapping("/maquinas/list")
    public String maquinasList() {
        return "maquinas/list.html";
    }

    @GetMapping("/historico-manutencao/list")
    public String historicoManutencaoList() {
        return "historico-manutencao/list.html";
    }

    @GetMapping("/telemetria/list")
    public String telemetriaList() {
        return "telemetria/list.html";
    }
}