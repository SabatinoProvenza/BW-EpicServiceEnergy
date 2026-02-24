package sabatinoprovenza.BW_EpicServiceEnergy.service;

import org.springframework.stereotype.Service;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Utente;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.NotFoundException;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.UtenteRepository;

import java.util.UUID;

@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public Utente findById(UUID id) {
        return this.utenteRepository.findById(id).orElseThrow(() -> new NotFoundException("L'utente con id: " + id + " non è stato trovato!"));
    }
}
