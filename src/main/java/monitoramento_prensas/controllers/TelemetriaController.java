package monitoramento_prensas.controllers;

import monitoramento_prensas.exceptions.ObjetoNaoEncontradoException;
import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.dtos.MaquinaDTO;
import monitoramento_prensas.models.dtos.TelemetriaDTO;
import monitoramento_prensas.services.TelemetriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Object> getTelemetria(@RequestParam Integer idTelemetria) {
        try {
            final TelemetriaDTO dto = this.telemetriaService.getTelemetriaDTO(idTelemetria);
            return ResponseEntity.ok(dto);
        } catch (ObjetoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar buscar o cadastro da telemetria. Tente novamente");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateTelemetria(@RequestBody TelemetriaDTO dto){
        try {
            this.telemetriaService.updateTelemetria(dto);
            return ResponseEntity.ok("Telemetria atualizada com sucesso.");
        } catch (ObjetoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar atualizar o cadastro da telemetria. Tente novamente");
        }
    }
}
