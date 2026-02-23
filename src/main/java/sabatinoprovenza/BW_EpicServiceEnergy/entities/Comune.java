package sabatinoprovenza.BW_EpicServiceEnergy.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Comune {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "sigla_provincia", nullable = false)
    private Provincia provincia;
}
