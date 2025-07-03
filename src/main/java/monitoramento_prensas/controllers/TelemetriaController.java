package monitoramento_prensas.controllers;

import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.dtos.TelemetriaDTO;
import monitoramento_prensas.services.TelemetriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de Telemetria
 *
 * @author Carlos Vieira
 * @since 02/07/2025
 */
@RestController
@RequestMapping(name = "/telemetria")
public class TelemetriaController {

    private TelemetriaService telemetriaService;

    public TelemetriaController(TelemetriaService telemetriaService){
        this.telemetriaService = telemetriaService;
    }

    @PostMapping
    public ResponseEntity<String> createTelemetria(@RequestBody TelemetriaDTO dto) {
        try {
            this.telemetriaService.createTelemetria(dto);
            return ResponseEntity.ok("Telemetria cadastrada com sucesso.");
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar criar o cadastro da telemetria. Tente novamente");
        }
    }
}
