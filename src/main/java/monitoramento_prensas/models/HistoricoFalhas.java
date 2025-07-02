package monitoramento_prensas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

}
