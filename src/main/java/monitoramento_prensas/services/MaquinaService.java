package monitoramento_prensas.services;

import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.Maquina;
import monitoramento_prensas.models.dtos.MaquinaDTO;
import monitoramento_prensas.repositories.MaquinaRepository;
import org.springframework.stereotype.Service;

/**
 * Service de {@link Maquina}.
 *
 * @author Carlos Vieira
 * @since 01/07/2025
 */
@Service
public class MaquinaService {

    private MaquinaRepository maquinaRepository;

    public MaquinaService(MaquinaRepository maquinaRepository) {
        this.maquinaRepository = maquinaRepository;
    }

    /**
     * Cria uma nova máquina no banco de dados
     *
     * @param dto
     * @throws PersistenceException
     */
    public void createMaquina(MaquinaDTO dto) {
        try {
            final Maquina maquina = new Maquina(dto.nome(), dto.nome(), dto.fabricante(), dto.dataCompra());
            this.maquinaRepository.save(maquina);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível criar o cadastro da máquina. Verifique!");
        }
    }
}
