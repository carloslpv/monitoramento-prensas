package monitoramento_prensas.controllers;

import monitoramento_prensas.exceptions.ObjetoNaoEncontradoException;
import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.dtos.HistoricoFalhasDTO;
import monitoramento_prensas.services.HistoricoFalhasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller de HistoricoFalhas
 *
 * @author Vinicius Begnini
 * @since 02/07/2025
 */
@RestController
@RequestMapping("/historicoFalhas")
public class HistoricoFalhasController {

    private final HistoricoFalhasService falhaService;

    public HistoricoFalhasController(HistoricoFalhasService falhaService) {
        this.falhaService = falhaService;
    }

    @PostMapping
    public ResponseEntity<String> createFalha(@RequestBody HistoricoFalhasDTO dto) {
        try {
            this.falhaService.createFalha(dto);
            return ResponseEntity.ok("Falha de máquina registrada com sucesso.");
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar registrar a falha da máquina. Tente novamente");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getFalha(@RequestParam Integer id) {
        try {
            final HistoricoFalhasDTO dto = falhaService.getFalhaDTO(id);
            return ResponseEntity.ok(dto);
        } catch (ObjetoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar buscar o registro das falhas de máquinas. Tente novamente");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateFalha(@RequestBody HistoricoFalhasDTO dto) {
        try {
            this.falhaService.updateFalha(dto);
            return ResponseEntity.ok("Registro de falha atualizado com sucesso.");
        } catch (ObjetoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar atualizar o registro da falha da máquina. Tente novamente");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFalha(@RequestBody Integer id){
        try {
            this.falhaService.deleteFalha(id);
            return ResponseEntity.ok("Falha atualizada com sucesso.");
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar remover o registro da falha. Tente novamente");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<HistoricoFalhasDTO>> getAllFalhas() {
        List<HistoricoFalhasDTO> falhas = this.falhaService.getAllHistoricoFalhasDTO();
        return ResponseEntity.ok(falhas);
    }
}
