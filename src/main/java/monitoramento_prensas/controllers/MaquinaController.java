package monitoramento_prensas.controllers;

import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.dtos.MaquinaDTO;
import monitoramento_prensas.services.MaquinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de Maquina
 *
 * @author Carlos Vieira
 * @since 01/07/2025
 */
@RestController
@RequestMapping("/maquina")
public class MaquinaController {

    private MaquinaService maquinaService;

    public MaquinaController(MaquinaService maquinaService) {
        this.maquinaService = maquinaService;
    }

    @PostMapping
    public ResponseEntity<String> createMaquina(@RequestBody MaquinaDTO dto) {
        try {
            this.maquinaService.createMaquina(dto);
            return ResponseEntity.ok("Máquina cadastrada com sucesso.");
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar criar o cadastro da máquina. Tente novamente");
        }
    }

}
