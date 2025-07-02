package monitoramento_prensas.repositories;

import monitoramento_prensas.models.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository de {@link Maquina}
 *
 * @author Carlos Vieira
 * @since 01/07/2025
 */
public interface MaquinaRepository extends JpaRepository<Maquina, Integer> {
}
