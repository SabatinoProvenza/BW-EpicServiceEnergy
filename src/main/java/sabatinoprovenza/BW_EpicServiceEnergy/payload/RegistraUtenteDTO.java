package sabatinoprovenza.BW_EpicServiceEnergy.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegistraUtenteDTO(
        @NotBlank(message = "L'username è obbligatorio")
        String username,
        @NotBlank
        String email,
        @NotBlank
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{4,}$", message = "La password deve contenere una maiuscola, una minuscola ecc ecc ...")
        String password,
        @NotBlank(message = "Il nome è obbligatorio")
        String nome,
        @NotBlank(message = "Il cognome è obbligatorio")
        String cognome

) {
}