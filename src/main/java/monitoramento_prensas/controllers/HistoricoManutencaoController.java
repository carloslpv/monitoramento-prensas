package monitoramento_prensas.controllers;

import monitoramento_prensas.exceptions.ObjetoNaoEncontradoException;
import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.dtos.HistoricoFalhasDTO;
import monitoramento_prensas.models.dtos.HistoricoManutencaoDTO;
import monitoramento_prensas.models.dtos.MaquinaDTO;
import monitoramento_prensas.services.HistoricoManutencaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return ResponseEntity.ok("Manutenção cadastrada com sucesso.");
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar criar o cadastro do histórico de manutenção. Tente novamente");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getHistoricoManutencao(@RequestParam Integer idHistoricoManutencao) {
        try {
            final HistoricoManutencaoDTO dto = this.historicoManutencaoService.getHistoricoManutencaoDTO(idHistoricoManutencao);
            return ResponseEntity.ok(dto);
        } catch (ObjetoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar buscar o cadastro do histórico de manutenção. Tente novamente");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateHistoricoManutencao(@RequestBody HistoricoManutencaoDTO dto){
        try {
            this.historicoManutencaoService.updateHistoricoManutencao(dto);
            return ResponseEntity.ok("Histórico de manutencao atualizada com sucesso.");
        } catch (ObjetoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar atualizar o histórico de manutencao. Tente novamente");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteHistoricoManutencao(@RequestBody Integer id){
        try {
            this.historicoManutencaoService.deleteHistoricoManutencao(id);
            return ResponseEntity.ok("Máquina atualizada com sucesso.");
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar remover o histórico de manutenção. Tente novamente");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<HistoricoManutencaoDTO>> getAllManutencao() {
        List<HistoricoManutencaoDTO> manutencao = this.historicoManutencaoService.getAllHistoricoManutencaoDTO();
        return ResponseEntity.ok(manutencao);
    }
}
