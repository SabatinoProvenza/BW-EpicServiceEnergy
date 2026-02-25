package sabatinoprovenza.BW_EpicServiceEnergy.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "fatture")
@Data
@NoArgsConstructor
public class Fattura {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, scale = 2)
    private double importo;

    @Column(unique = true, nullable = false)
    private String numero;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "stato_fattura_id")
    private StatoFattura statoFattura;

}
