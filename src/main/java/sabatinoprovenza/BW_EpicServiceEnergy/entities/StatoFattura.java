package sabatinoprovenza.BW_EpicServiceEnergy.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "statoFatture")
@Data
@NoArgsConstructor
public class StatoFattura {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nomeStato;

}
