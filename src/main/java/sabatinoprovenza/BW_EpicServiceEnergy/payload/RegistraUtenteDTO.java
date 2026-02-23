package sabatinoprovenza.BW_EpicServiceEnergy.payload;

import jakarta.validation.constraints.NotBlank;

public record RegistraUtenteDTO(
        @NotBlank(message = "L'username è obbligatorio")
        String username,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank(message = "Il nome è obbligatorio")
        String nome,
        @NotBlank(message = "Il cognome è obbligatorio")
        String cognome

) {
}