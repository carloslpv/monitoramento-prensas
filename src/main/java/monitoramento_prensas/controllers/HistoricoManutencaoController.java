package monitoramento_prensas.controllers;

import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.dtos.HistoricoManutencaoDTO;
import monitoramento_prensas.services.HistoricoManutencaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de HistoricoManutencao
 *
 * @author Carlos Vieira
 * @since 02/07/2025
 */
@RestController
@RequestMapping("/historico-manutencao")
public class HistoricoManutencaoController {

    private HistoricoManutencaoService historicoManutencaoService;

    public HistoricoManutencaoController(HistoricoManutencaoService historicoManutencaoService) {
        this.historicoManutencaoService = historicoManutencaoService;
    }

    @PostMapping
    public ResponseEntity<String> createHistoricoManutencao(@RequestBody HistoricoManutencaoDTO dto) {
        try {
            this.historicoManutencaoService.createHistoricoManutencao(dto);
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
