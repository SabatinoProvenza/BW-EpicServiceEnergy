package sabatinoprovenza.BW_EpicServiceEnergy.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "province")
@Data
@NoArgsConstructor
public class Provincia {
    @Id
//    @Setter(AccessLevel.NONE)
    @Column(nullable = false) //length = 2)
    private String sigla;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String regione;

    public Provincia(String sigla, String nome, String regione) {
        this.sigla = sigla;
        this.nome = nome;
        this.regione = regione;
    }
}
