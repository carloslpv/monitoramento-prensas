package monitoramento_prensas.controllers;

import monitoramento_prensas.exceptions.ObjetoNaoEncontradoException;
import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.dtos.HistoricoManutencaoDTO;
import monitoramento_prensas.models.dtos.MaquinaDTO;
import monitoramento_prensas.services.MaquinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<Object> getMaquina(@RequestParam("idMaquina") Integer idMaquina) {
        try {
            MaquinaDTO dto = this.maquinaService.getMaquinaDTO(idMaquina);
            return ResponseEntity.ok(dto);
        } catch (ObjetoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar máquina.");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateMaquina(@RequestBody MaquinaDTO dto){
        try {
            this.maquinaService.updateMaquina(dto);
            return ResponseEntity.ok("Máquina removida com sucesso.");
        } catch (ObjetoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar atualizar o cadastro da máquina. Tente novamente");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMaquina(@RequestBody Integer id){
        try {
            this.maquinaService.deleteMaquina(id);
            return ResponseEntity.ok("Máquina removida com sucesso.");
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Algo deu errado ao tentar remover o cadastro da máquina. Tente novamente");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<MaquinaDTO>> getAllMaquina() {
        List<MaquinaDTO> maquina = this.maquinaService.getAllMaquinaDTO();
        return ResponseEntity.ok(maquina);
    }
}
