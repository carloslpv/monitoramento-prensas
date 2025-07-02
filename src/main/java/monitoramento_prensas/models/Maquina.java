package monitoramento_prensas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "maquinas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Maquina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String modelo;

    private String fabricante;

    @Column(name = "data_compra")
    private LocalDateTime dataCompra;

}
