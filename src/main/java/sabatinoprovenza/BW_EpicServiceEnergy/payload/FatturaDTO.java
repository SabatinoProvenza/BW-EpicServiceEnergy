package sabatinoprovenza.BW_EpicServiceEnergy.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.UUID;

public record FatturaDTO(
        @NotNull(message = "La data è obbligatoria")
        LocalDate data,
        @NotNull(message = "L'importo è obbligatorio")
        @Positive(message = "L'importo deve essere un numero positivo")
        double importo,
        @NotBlank(message = "Il numero è obbligatorio")
        String numero,
        @NotNull(message = "Il cliente è obbligatorio")
        UUID clienteId,
        UUID statoFatturaId
) {
}
