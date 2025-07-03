package monitoramento_prensas.repositories;

import monitoramento_prensas.models.HistoricoManutencao;
import monitoramento_prensas.models.Telemetria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository de {@link HistoricoManutencao}.
 *
 * @author Carlos Vieira
 * @since 02/07/2025
 */
public interface HistoricoManutencaoRepository extends JpaRepository<HistoricoManutencao, Integer> {
}
