package monitoramento_prensas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
