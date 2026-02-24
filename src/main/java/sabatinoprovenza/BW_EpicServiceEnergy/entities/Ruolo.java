package sabatinoprovenza.BW_EpicServiceEnergy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ruoli")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeRuolo; // ROLE_USER, ROLE_ADMIN

    public Ruolo(String nomeRuolo) {
        this.nomeRuolo = nomeRuolo;
    }
}