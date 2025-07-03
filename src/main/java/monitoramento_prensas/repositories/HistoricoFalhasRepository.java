package monitoramento_prensas.repositories;

import monitoramento_prensas.models.HistoricoFalhas;
import monitoramento_prensas.models.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository de {@link HistoricoFalhas}
 *
 * @author Vinicius Begnini
 * @since 02/07/2025
 */
public interface HistoricoFalhasRepository extends JpaRepository<HistoricoFalhas, Integer> {
}
