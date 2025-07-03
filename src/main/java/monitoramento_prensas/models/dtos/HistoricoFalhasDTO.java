package monitoramento_prensas.models.dtos;

import java.time.LocalDateTime;

/**
 * Data Transfer Object de HistoricoFalhas
 *
 * @author Vinicius Begnini
 * @since 02/07/2025
 */
public record HistoricoFalhasDTO(
        Integer id,
        Integer idMaquina,
        String tipoFalha,
        LocalDateTime dataFalha
) {
}
