package sabatinoprovenza.BW_EpicServiceEnergy.exceptions;

public class NotEmptyException extends RuntimeException {
    public NotEmptyException() {
        super("Il file non puo' essere vuoto!");
    }
}
