package sabatinoprovenza.BW_EpicServiceEnergy.payload;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProvinciaPayload {
    @CsvBindByName(column = "sigla")
    private String sigla;

    @CsvBindByName(column = "nome")
    private String nome;

    @CsvBindByName(column = "regione")
    private String regione;
}
