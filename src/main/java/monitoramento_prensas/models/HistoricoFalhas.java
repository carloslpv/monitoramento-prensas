package monitoramento_prensas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import monitoramento_prensas.models.dtos.HistoricoFalhasDTO;
import monitoramento_prensas.models.dtos.MaquinaDTO;

import java.time.LocalDateTime;

/**
 * Entidade de HistoricoFalhas.
 *
 * @author Vinicius Begnini
 * @since 01/07/2025
 */
@Entity
@Table(name = "historico_falhas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoricoFalhas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_maquina")
    private Maquina maquina;

    @Column(name = "tipo_falha")
    private String tipoFalha;

    @Column(name = "data_falha")
    private LocalDateTime dataFalha;

    /**
     * Retorna um objeto {@link monitoramento_prensas.models.dtos.HistoricoFalhasDTO}
     *
     * @return
     */
    public HistoricoFalhasDTO toDTO() {
        return new HistoricoFalhasDTO(
                this.id,
                this.maquina != null ? this.maquina.getId() : null,
                this.tipoFalha,
                this.dataFalha
        );
    }
}
