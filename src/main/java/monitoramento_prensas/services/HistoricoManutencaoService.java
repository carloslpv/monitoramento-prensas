package monitoramento_prensas.services;

import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.HistoricoFalhas;
import monitoramento_prensas.models.HistoricoManutencao;
import monitoramento_prensas.models.Maquina;
import monitoramento_prensas.models.Telemetria;
import monitoramento_prensas.models.dtos.HistoricoManutencaoDTO;
import monitoramento_prensas.models.dtos.TelemetriaDTO;
import monitoramento_prensas.repositories.HistoricoManutencaoRepository;
import org.springframework.stereotype.Service;

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

    public HistoricoManutencaoService(){
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
            HistoricoFalhas historicoFalha = new HistoricoFalhas();
            if(dto.idHistoricoFalha() != null){
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
}
