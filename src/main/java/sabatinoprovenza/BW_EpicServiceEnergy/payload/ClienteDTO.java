package sabatinoprovenza.BW_EpicServiceEnergy.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ClienteDTO(
        @NotBlank
        String ragioneSociale,
        @NotBlank
        String partitaIva,
        @NotBlank
        @Email
        String email,
        @NotNull
        @Positive
        double fatturatoAnnuale,
        @NotBlank
        String pec,
        @NotBlank
        String telefono,
        @NotBlank
        String emailContatto,
        @NotBlank
        String nomeContatto,
        @NotBlank
        String cognomeContatto,
        @NotBlank
        String telefonoContatto,
        @NotBlank
        String tipoCliente, // "PA", "SRL", ecc.

        // Indirizzo Sede Legale
        @NotBlank
        String viaLegale,
        @NotBlank
        String civicoLegale,
        @NotBlank
        String capLegale,
        @NotBlank
        String nomeComuneLegale, // Uso il nome per trovarlo tramite DB

        // Indirizzo Sede Operativa (Opzionale)
        String viaOperativa,
        String civicoOperativa,
        String capOperativa,
        String nomeComuneOperativa

) {
}
