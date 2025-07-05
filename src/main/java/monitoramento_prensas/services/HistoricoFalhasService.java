package monitoramento_prensas.services;

import monitoramento_prensas.exceptions.ObjetoNaoEncontradoException;
import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.HistoricoFalhas;
import monitoramento_prensas.models.Maquina;
import monitoramento_prensas.models.dtos.HistoricoFalhasDTO;
import monitoramento_prensas.repositories.HistoricoFalhasRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service de {@link HistoricoFalhas}.
 *
 * @author Vinicius Begnini
 * @since 02/07/2025
 */
@Service
public class HistoricoFalhasService {

    private HistoricoFalhasRepository historicoFalhasRepository;
    private MaquinaService maquinaService;

    public HistoricoFalhasService(HistoricoFalhasRepository historicoFalhasRepository, MaquinaService maquinaService) {
        this.historicoFalhasRepository = historicoFalhasRepository;
        this.maquinaService = maquinaService;
    }

    /**
     * Cria uma nova falha no banco de dados
     *
     * @param dto
     * @throws PersistenceException
     */
    public void createFalha(HistoricoFalhasDTO dto) {
        try {
            Maquina maquina = maquinaService.getMaquina(dto.idMaquina());

            HistoricoFalhas falha = new HistoricoFalhas();
            falha.setMaquina(maquina);
            falha.setTipoFalha(dto.tipoFalha());
            falha.setDataFalha(dto.dataFalha());

            historicoFalhasRepository.save(falha);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível registrar falha. Verifique!");
        }
    }

    /**
     * Retorna uma {@link HistoricoFalhasDTO} a partir do id de uma falha.
     *
     * @param id
     * @return
     * @throws ObjetoNaoEncontradoException
     */
    public HistoricoFalhasDTO getFalhaDTO(Integer id) {
        final Optional<HistoricoFalhas> falhasOptional = historicoFalhasRepository.findById(id);
        if (falhasOptional.isEmpty()) {
            throw new ObjetoNaoEncontradoException("Não foi encontrada nenhuma falha com este id");
        }
        return falhasOptional.get().toDTO();
    }

    /**
     * Retorna uma {@link HistoricoFalhas} a partir do id de uma falha.
     *
     * @param id
     * @return
     * @throws ObjetoNaoEncontradoException
     */
    public HistoricoFalhas getFalha(final Integer id) throws ObjetoNaoEncontradoException {
        return historicoFalhasRepository.findById(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException("Não foi encontrada nenhuma falha com este id"));
    }

    /**
     * Atualiza o cadastro de uma {@link HistoricoFalhas}.
     *
     * @param dto
     * @throws ObjetoNaoEncontradoException
     * @throws PersistenceException
     */
    public void updateFalha(HistoricoFalhasDTO dto) {
        try {
            HistoricoFalhas falha = historicoFalhasRepository.findById(dto.id())
                    .orElseThrow(() -> new ObjetoNaoEncontradoException("Falha não encontrada para atualização."));
            Maquina maquina = maquinaService.getMaquina(dto.idMaquina());

            falha.setMaquina(maquina);
            falha.setTipoFalha(dto.tipoFalha());
            falha.setDataFalha(dto.dataFalha());
            historicoFalhasRepository.save(falha);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível atualizar o registro da falha. Verifique!");
        }
    }

    /**
     * Remove o cadastro de uma {@link HistoricoFalhas}.
     *
     * @param id
     * @throws PersistenceException
     */
    public void deleteFalha(Integer id) {
        try {
            historicoFalhasRepository.deleteById(id);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível remover o registro da falha. Verifique!");
        }
    }

    /**
     * Retorna uma lista de {@link HistoricoFalhasDTO}
     *
     * @return
     */
    public List<HistoricoFalhasDTO> getAllHistoricoFalhasDTO() {
        return this.historicoFalhasRepository.findAll().stream().map(HistoricoFalhas::toDTO).collect(Collectors.toList());
    }
}

