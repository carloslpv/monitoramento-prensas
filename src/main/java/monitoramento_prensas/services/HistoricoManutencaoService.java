package monitoramento_prensas.services;

import monitoramento_prensas.exceptions.ObjetoNaoEncontradoException;
import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.HistoricoFalhas;
import monitoramento_prensas.models.HistoricoManutencao;
import monitoramento_prensas.models.Maquina;
import monitoramento_prensas.models.dtos.HistoricoManutencaoDTO;
import monitoramento_prensas.repositories.HistoricoManutencaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service de {@link HistoricoManutencao}.
 *
 * @author Carlos Vieira
 * @since 02/07/2025
 */
@Service
public class HistoricoManutencaoService {

    private HistoricoManutencaoRepository historicoManutencaoRepository;
    private MaquinaService maquinaService;
    private HistoricoFalhasService historicoFalhasService;

    public HistoricoManutencaoService(HistoricoManutencaoRepository historicoManutencaoRepository,
                                      MaquinaService maquinaService,
                                      HistoricoFalhasService historicoFalhasService) {
        this.historicoManutencaoRepository = historicoManutencaoRepository;
        this.maquinaService = maquinaService;
        this.historicoFalhasService = historicoFalhasService;
    }

    /**
     * Cria uma novo {@link HistoricoManutencao} no banco de dados
     *
     * @param dto
     * @throws PersistenceException
     */
    public void createHistoricoManutencao(HistoricoManutencaoDTO dto) {
        try {
            final Maquina maquina = this.maquinaService.getMaquina(dto.idMaquina());
            HistoricoFalhas historicoFalha = null;
            if (dto.idHistoricoFalha() != null) {
                historicoFalha = this.historicoFalhasService.getFalha(dto.idHistoricoFalha());
            }
            final HistoricoManutencao historicoManutencao = new HistoricoManutencao(
                    maquina,
                    dto.tipoManutencao(),
                    dto.acaoRealizada(),
                    dto.dataHoraManutencao(),
                    dto.tempoManutencaoMin(),
                    historicoFalha);
            this.historicoManutencaoRepository.save(historicoManutencao);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível criar o cadastro do histórico de manutenção. Verifique!");
        }
    }

    /**
     * Retorna uma {@link HistoricoManutencaoDTO} a partir do id de uma de um historico de manutencao.
     *
     * @param idHistoricoManutencao
     * @return
     * @throws ObjetoNaoEncontradoException
     */
    public HistoricoManutencaoDTO getHistoricoManutencaoDTO(final Integer idHistoricoManutencao) throws ObjetoNaoEncontradoException {
        final Optional<HistoricoManutencao> optionalHistoricoManutencao = this.historicoManutencaoRepository.findById(idHistoricoManutencao);
        if (optionalHistoricoManutencao.isEmpty()) {
            throw new ObjetoNaoEncontradoException("Não foi encontrada nenhum histórico de manutenção com este id");
        }
        return optionalHistoricoManutencao.get().toDTO();
    }

    /**
     * Retorna uma {@link HistoricoManutencao} a partir do id de um historico de manutencao.
     *
     * @param idHistoricoManutencao
     * @return
     * @throws ObjetoNaoEncontradoException
     */
    public HistoricoManutencao getHistoricoManutencao(final Integer idHistoricoManutencao) throws ObjetoNaoEncontradoException {
        final Optional<HistoricoManutencao> optionalHistoricoManutencao = this.historicoManutencaoRepository.findById(idHistoricoManutencao);
        if (optionalHistoricoManutencao.isEmpty()) {
            throw new ObjetoNaoEncontradoException("Não foi encontrada nenhum histórico de manutenção com este id");
        }
        return optionalHistoricoManutencao.get();
    }

    /**
     * Atualiza o cadastro de um {@link HistoricoManutencao}.
     *
     * @param dto
     * @throws ObjetoNaoEncontradoException
     * @throws PersistenceException
     */
    public void updateHistoricoManutencao(final HistoricoManutencaoDTO dto) throws ObjetoNaoEncontradoException, PersistenceException {
        try {
            final HistoricoManutencao historicoManutencao = this.getHistoricoManutencao(dto.id());
            Maquina maquina;
            if(historicoManutencao.getMaquina().getId() != dto.idMaquina()){
                maquina = this.maquinaService.getMaquina(dto.idMaquina());
                historicoManutencao.setMaquina(maquina);
            }
            HistoricoFalhas historicoFalhas;
            if(historicoManutencao.getHistoricoFalha() != null && historicoManutencao.getHistoricoFalha().getId() !=
                    dto.idHistoricoFalha()) {
                historicoFalhas = this.historicoFalhasService.getFalha(dto.idHistoricoFalha());
                historicoManutencao.setHistoricoFalha(historicoFalhas);
            }
            historicoManutencao.setTipoManutencao(dto.tipoManutencao());
            historicoManutencao.setAcaoRealizada(dto.acaoRealizada());
            historicoManutencao.setDataHoraManutencao(dto.dataHoraManutencao());
            historicoManutencao.setTempoManutencaoMin(dto.tempoManutencaoMin());
            this.historicoManutencaoRepository.save(historicoManutencao);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível atualizar o cadastro do histórico de manutenção. Verifique!");
        }
    }

    /**
     * Remove o cadastro de um {@link HistoricoManutencao}.
     *
     * @param id
     * @throws PersistenceException
     */
    public void deleteHistoricoManutencao(final Integer id) throws PersistenceException {
        try {
            this.historicoManutencaoRepository.deleteById(id);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível remover o histórico de manutenção. Verifique!");
        }
    }

    /**
     * Retorna uma lista de {@link HistoricoManutencaoDTO}
     *
     * @return
     */
    public List<HistoricoManutencaoDTO> getAllHistoricoManutencaoDTO() {
        return this.historicoManutencaoRepository.findAll().stream().map(HistoricoManutencao::toDTO).collect(Collectors.toList());
    }
}
