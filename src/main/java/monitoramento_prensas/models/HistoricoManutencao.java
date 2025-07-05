package monitoramento_prensas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import monitoramento_prensas.models.dtos.HistoricoManutencaoDTO;
import monitoramento_prensas.models.dtos.MaquinaDTO;

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

    public HistoricoManutencao(Maquina maquina,
                               String tipoManutencao,
                               String acaoRealizada,
                               LocalDateTime dataHoraManutencao,
                               Integer tempoManutencaoMin,
                               HistoricoFalhas historicoFalha) {
        this.maquina = maquina;
        this.tipoManutencao = tipoManutencao;
        this.acaoRealizada = acaoRealizada;
        this.dataHoraManutencao = dataHoraManutencao;
        this.tempoManutencaoMin = tempoManutencaoMin;
        this.historicoFalha = historicoFalha;
    }

    /**
     * Retorna um objeto {@link HistoricoManutencaoDTO} a partir do historico de manutencao
     *
     * @return
     */
    public HistoricoManutencaoDTO toDTO(){
        return new HistoricoManutencaoDTO(
                this.id,
                this.maquina.getId(),
                this.tipoManutencao,
                this.acaoRealizada,
                this.dataHoraManutencao,
                this.tempoManutencaoMin,
                this.historicoFalha != null ? this.historicoFalha.getId() : null
        );
    }
}
