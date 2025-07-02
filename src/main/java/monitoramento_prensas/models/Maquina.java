package monitoramento_prensas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import monitoramento_prensas.models.dtos.MaquinaDTO;

import java.time.LocalDateTime;

/**
 * Entidade de Maquina.
 *
 * @author Carlos Vieira
 * @since 01/07/2025
 */
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

    public Maquina(String nome, String modelo, String fabricante, LocalDateTime dataCompra) {
        this.nome = nome;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.dataCompra = dataCompra;
    }

    /**
     * Retorna um objeto {@link MaquinaDTO} a partir da máquina
     *
     * @return
     */
    public MaquinaDTO toDTO() {
        return new MaquinaDTO(
                this.id,
                this.nome,
                this.modelo,
                this.fabricante,
                this.dataCompra
        );
    }
}
