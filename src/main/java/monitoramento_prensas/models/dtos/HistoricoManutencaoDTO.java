package monitoramento_prensas.models.dtos;

import java.time.LocalDateTime;

/**
 * Data Transfer Object de Historico Manutencao
 *
 * @author Carlos Vieira
 * @since 02/07/2025
 */
public record HistoricoManutencaoDTO(
        Integer id,
        Integer idMaquina,
        String tipoManutencao,
        String acaoRealizada,
        LocalDateTime dataHoraManutencao,
        Integer tempoManutencaoMin,
        Integer idHistoricoFalha
) {
}
