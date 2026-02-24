package sabatinoprovenza.BW_EpicServiceEnergy.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Utente;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.BadRequestException;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.NotFoundException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.RegistraUtenteDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.UtenteRepository;

import java.util.UUID;

@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder bcrypt;

    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder bcrypt) {
        this.utenteRepository = utenteRepository;
        this.bcrypt = bcrypt;
    }

    public Utente findById(UUID id) {
        return this.utenteRepository.findById(id).orElseThrow(() -> new NotFoundException("L'utente con id: " + id + " non è stato trovato!"));
    }

    public Utente findByUsername(String username) {
        return this.utenteRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("L'utente con username: " + username + " non è stato trovato!"));
    }


    public Utente saveUtente(RegistraUtenteDTO utenteDTO) {

        this.utenteRepository.findByEmail(utenteDTO.email()).ifPresent(utente -> {
            throw new BadRequestException("L'email " + utente.getEmail() + " è già in uso!");
        });

        Utente newUtente = new Utente(
                utenteDTO.username(),
                utenteDTO.email(),
                bcrypt.encode(utenteDTO.password()),
                utenteDTO.nome(),
                utenteDTO.cognome()
        );

        Utente savedUtente = this.utenteRepository.save(newUtente);

        System.out.println("L'utente " + savedUtente.getNome() + " " + savedUtente.getCognome() + " è stato aggiunto correttamente al DB!");

        return savedUtente;
    }

}
