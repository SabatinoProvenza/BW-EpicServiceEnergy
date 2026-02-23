package sabatinoprovenza.BW_EpicServiceEnergy.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Indirizzo {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String via;

    @Column(nullable = false)
    private String civico;

    @Column(nullable = false)
    private String cap;

    @Column(nullable = false)
    private String localita;

    @ManyToOne
    @JoinColumn(name = "comune_id", nullable = false)
    private Comune comune;
}
