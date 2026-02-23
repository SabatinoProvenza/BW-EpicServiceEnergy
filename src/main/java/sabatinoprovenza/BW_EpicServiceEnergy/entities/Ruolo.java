package sabatinoprovenza.BW_EpicServiceEnergy.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ruoli")
@Data
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nomeRuolo; // ROLE_USER, ROLE_ADMIN
}