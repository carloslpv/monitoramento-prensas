package monitoramento_prensas.services;

import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.Maquina;
import monitoramento_prensas.models.Telemetria;
import monitoramento_prensas.models.dtos.TelemetriaDTO;
import monitoramento_prensas.repositories.TelemetriaRepository;
import org.springframework.stereotype.Service;

/**
 * Service de Telemetria.
 *
 * @author Carlos Vieira
 * @since 02/07/2025
 */
@Service
public class TelemetriaService {

    private TelemetriaRepository telemetriaRepository;
    private MaquinaService maquinaService;

    public TelemetriaService(TelemetriaRepository telemetriaRepository, MaquinaService maquinaService){
        this.telemetriaRepository = telemetriaRepository;
        this.maquinaService = maquinaService;
    }

    /**
     * Cria uma nova {@link Telemetria} no banco de dados
     *
     * @param dto
     * @throws PersistenceException
     */
    public void createTelemetria(TelemetriaDTO dto) {
        try {
            final Maquina maquina = this.maquinaService.getMaquina(dto.idMaquina());
            final Telemetria telemetria = new Telemetria(
                    dto.sensorNivelBaixo(),
                    dto.pressaoHidraulica(),
                    dto.temperaturaOleo(),
                    dto.ciclosOperacao(),
                    dto.vibracao(),
                    dto.dataHoraColeta(),
                    dto.nomeSensor(),
                    dto.maquinaLigada(),
                    maquina);
            this.telemetriaRepository.save(telemetria);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível criar o cadastro da telemetria. Verifique!");
        }
    }
}
