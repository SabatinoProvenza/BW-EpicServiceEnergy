package sabatinoprovenza.BW_EpicServiceEnergy.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Provincia {
    @Id
    @Column(nullable = false, length = 2)
    private String sigla;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String regione;
}
