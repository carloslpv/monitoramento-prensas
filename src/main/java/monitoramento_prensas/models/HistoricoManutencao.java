package monitoramento_prensas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_manutencao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoricoManutencao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_maquina")
    private Maquina maquina;

    @Column(name = "tipo_manutencao")
    private String tipoManutencao;

    @Column(name = "acao_realizada")
    private String acaoRealizada;

    @Column(name = "data_hora_manutencao")
    private LocalDateTime dataHoraManutencao;

    @Column(name = "tempo_manutencao_min")
    private Integer tempoManutencaoMin;

    @ManyToOne
    @JoinColumn(name = "id_falha")
    private HistoricoFalhas historicoFalha;
}
