package monitoramento_prensas.services;

import monitoramento_prensas.exceptions.ObjetoNaoEncontradoException;
import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.Maquina;
import monitoramento_prensas.models.Telemetria;
import monitoramento_prensas.models.dtos.TelemetriaDTO;
import monitoramento_prensas.repositories.TelemetriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public TelemetriaService(TelemetriaRepository telemetriaRepository, MaquinaService maquinaService) {
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

    /**
     * Retorna uma {@link TelemetriaDTO} a partir do id de uma telemetria.
     *
     * @param idTelemetria
     * @return
     * @throws ObjetoNaoEncontradoException
     */
    public TelemetriaDTO getTelemetriaDTO(final Integer idTelemetria) throws ObjetoNaoEncontradoException {
        final Optional<Telemetria> optionalTelemetria = this.telemetriaRepository.findById(idTelemetria);
        if (optionalTelemetria.isEmpty()) {
            throw new ObjetoNaoEncontradoException("Não foi encontrada nenhuma telemetria com este id");
        }
        return optionalTelemetria.get().toDTO();
    }

    /**
     * Retorna uma {@link Telemetria} a partir do id de uma telemetria.
     *
     * @param idTelemetria
     * @return
     * @throws ObjetoNaoEncontradoException
     */
    public Telemetria getTelemetria(final Integer idTelemetria) throws ObjetoNaoEncontradoException {
        final Optional<Telemetria> optionalTelemetria = this.telemetriaRepository.findById(idTelemetria);
        if (optionalTelemetria.isEmpty()) {
            throw new ObjetoNaoEncontradoException("Não foi encontrada nenhuma telemetria com este id");
        }
        return optionalTelemetria.get();
    }

    /**
     * Atualiza o cadastro de uma {@link Telemetria}.
     *
     * @param dto
     * @throws ObjetoNaoEncontradoException
     * @throws PersistenceException
     */
    public void updateTelemetria(final TelemetriaDTO dto) throws ObjetoNaoEncontradoException, PersistenceException {
        try {
            final Telemetria telemetria = this.getTelemetria(dto.id());
            Maquina maquina = new Maquina();
            if (telemetria.getMaquina().getId() != dto.idMaquina()) {
                maquina = this.maquinaService.getMaquina(dto.idMaquina());
            }
            telemetria.setSensorNivelBaixo(dto.sensorNivelBaixo());
            telemetria.setPressaoHidraulica(dto.pressaoHidraulica());
            telemetria.setTemperaturaOleo(dto.temperaturaOleo());
            telemetria.setCiclosOperacao(dto.ciclosOperacao());
            telemetria.setVibracao(dto.vibracao());
            telemetria.setDataHoraColeta(dto.dataHoraColeta());
            telemetria.setNomeSensor(dto.nomeSensor());
            telemetria.setMaquinaLigada(dto.maquinaLigada());
            telemetria.setMaquina(maquina);
            this.telemetriaRepository.save(telemetria);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível atualizar o cadastro da telemetria. Verifique!");
        }
    }

    /**
     * Remove o cadastro de uma {@link Telemetria}.
     *
     * @param id
     * @throws PersistenceException
     */
    public void deleteTelemetria(final Integer id) throws PersistenceException {
        try {
            this.telemetriaRepository.deleteById(id);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível remover o cadastro da Telemetria. Verifique!");
        }
    }

    /**
     * Retorna uma lista de {@link TelemetriaDTO}
     *
     * @return
     */
    public List<TelemetriaDTO> getAllTelemetriaDTO() {
        return this.telemetriaRepository.findAll().stream().map(Telemetria::toDTO).collect(Collectors.toList());
    }
}
