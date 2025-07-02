package monitoramento_prensas.models.dtos;

import java.time.LocalDateTime;

/**
 * Data Transfer Object de Maquina
 *
 * @author Carlos Vieira
 * @since 01/07/2025
 */
public record MaquinaDTO(
        Integer id,
        String nome,
        String modelo,
        String fabricante,
        LocalDateTime dataCompra
) {
}
