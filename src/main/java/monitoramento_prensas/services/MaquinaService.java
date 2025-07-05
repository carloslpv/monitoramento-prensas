package monitoramento_prensas.services;

import monitoramento_prensas.exceptions.ObjetoNaoEncontradoException;
import monitoramento_prensas.exceptions.PersistenceException;
import monitoramento_prensas.models.Maquina;
import monitoramento_prensas.models.Telemetria;
import monitoramento_prensas.models.dtos.MaquinaDTO;
import monitoramento_prensas.models.dtos.TelemetriaDTO;
import monitoramento_prensas.repositories.MaquinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Retorna uma {@link MaquinaDTO} a partir do id de uma máquina.
     *
     * @param idMaquina
     * @return
     * @throws ObjetoNaoEncontradoException
     */
    public MaquinaDTO getMaquinaDTO(final Integer idMaquina) throws ObjetoNaoEncontradoException {
        final Optional<Maquina> optionalMaquina = this.maquinaRepository.findById(idMaquina);
        if (optionalMaquina.isEmpty()) {
            throw new ObjetoNaoEncontradoException("Não foi encontrada nenhuma máquina com este id");
        }
        return optionalMaquina.get().toDTO();
    }

    /**
     * Retorna uma {@link Maquina} a partir do id de uma máquina.
     *
     * @param idMaquina
     * @return
     * @throws ObjetoNaoEncontradoException
     */
    public Maquina getMaquina(final Integer idMaquina) throws ObjetoNaoEncontradoException {
        final Optional<Maquina> optionalMaquina = this.maquinaRepository.findById(idMaquina);
        if (optionalMaquina.isEmpty()) {
            throw new ObjetoNaoEncontradoException("Não foi encontrada nenhuma máquina com este id");
        }
        return optionalMaquina.get();
    }

    /**
     * Atualiza o cadastro de uma {@link Maquina}.
     *
     * @param dto
     * @throws ObjetoNaoEncontradoException
     * @throws PersistenceException
     */
    public void updateMaquina(final MaquinaDTO dto) throws ObjetoNaoEncontradoException, PersistenceException {
        try {
            final Maquina maquina = this.getMaquina(dto.id());
            maquina.setNome(dto.nome());
            maquina.setFabricante(dto.fabricante());
            maquina.setModelo(dto.modelo());
            maquina.setDataCompra(dto.dataCompra());
            this.maquinaRepository.save(maquina);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível atualizar o cadastro da máquina. Verifique!");
        }
    }

    /**
     * Remove o cadastro de uma {@link Maquina}.
     *
     * @param id
     * @throws PersistenceException
     */
    public void deleteMaquina(final Integer id) throws PersistenceException {
        try {
            this.maquinaRepository.deleteById(id);
        } catch (Exception e) {
            throw new PersistenceException("Não foi possível remover o cadastro da máquina. Verifique!");
        }
    }

    /**
     * Retorna uma lista de {@link MaquinaDTO}
     *
     * @return
     */
    public List<MaquinaDTO> getAllMaquinaDTO() {
        return this.maquinaRepository.findAll().stream().map(Maquina::toDTO).collect(Collectors.toList());
    }
}