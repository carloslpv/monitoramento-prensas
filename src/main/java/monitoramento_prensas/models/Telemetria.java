package monitoramento_prensas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import monitoramento_prensas.models.dtos.MaquinaDTO;
import monitoramento_prensas.models.dtos.TelemetriaDTO;

import java.time.LocalDateTime;

@Entity
@Table(name = "telemetria")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Telemetria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sensor_nivel_baixo")
    private Boolean sensorNivelBaixo;

    @Column(name = "pressao_hidraulica")
    private Float pressaoHidraulica;

    @Column(name = "temperatura_oleo")
    private Float temperaturaOleo;

    @Column(name = "ciclos_operacao")
    private Float ciclosOperacao;

    private Float vibracao;

    @Column(name = "data_hora_coleta")
    private LocalDateTime dataHoraColeta;

    @Column(name = "nome_sensor")
    private String nomeSensor;

    @Column(name = "maquina_ligada")
    private Boolean maquinaLigada;

    @ManyToOne
    @JoinColumn(name = "id_maquina")
    private Maquina maquina;

    public Telemetria(Boolean sensorNivelBaixo, Float pressaoHidraulica, Float temperaturaOleo,
                      Float ciclosOperacao, Float vibracao, LocalDateTime dataHoraColeta,
                      String nomeSensor, Boolean maquinaLigada, Maquina maquina) {
        this.sensorNivelBaixo = sensorNivelBaixo;
        this.pressaoHidraulica = pressaoHidraulica;
        this.temperaturaOleo = temperaturaOleo;
        this.ciclosOperacao = ciclosOperacao;
        this.vibracao = vibracao;
        this.dataHoraColeta = dataHoraColeta;
        this.nomeSensor = nomeSensor;
        this.maquinaLigada = maquinaLigada;
        this.maquina = maquina;
    }

    /**
     * Retorna um objeto {@link TelemetriaDTO} a partir da telemetria
     *
     * @return
     */
    public TelemetriaDTO toDTO(){
        return new TelemetriaDTO(
                this.id,
                this.sensorNivelBaixo,
                this.pressaoHidraulica,
                this.temperaturaOleo,
                this.ciclosOperacao,
                this.vibracao,
                this.dataHoraColeta,
                this.nomeSensor,
                this.maquinaLigada,
                this.maquina.getId()
        );
    }
}
