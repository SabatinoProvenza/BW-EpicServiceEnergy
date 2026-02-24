package sabatinoprovenza.BW_EpicServiceEnergy.payload;

import java.time.LocalDateTime;

public record ErrorDTO(String message, LocalDateTime timestamp) {
}
