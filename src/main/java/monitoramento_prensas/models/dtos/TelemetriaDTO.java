package monitoramento_prensas.models.dtos;

import java.time.LocalDateTime;

/**
 * Data Transfer Object de Telemetria.
 *
 * @author Carlos Vieira
 * @since 02/07/2025
 */
public record TelemetriaDTO(
        Integer id,
        Boolean sensorNivelBaixo,
        Float pressaoHidraulica,
        Float temperaturaOleo,
        Float ciclosOperacao,
        Float vibracao,
        LocalDateTime dataHoraColeta,
        String nomeSensor,
        Boolean maquinaLigada,
        Integer idMaquina
) {
}
