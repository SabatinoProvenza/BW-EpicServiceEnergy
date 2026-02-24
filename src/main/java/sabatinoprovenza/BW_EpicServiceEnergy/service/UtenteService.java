package sabatinoprovenza.BW_EpicServiceEnergy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Ruolo;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Utente;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.NotFoundException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.RegistraUtenteDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.RuoloRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.UtenteRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;
    @Autowired
    private RuoloRepository ruoloRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public Utente findById(UUID id) {
        return this.utenteRepository.findById(id).orElseThrow(() -> new NotFoundException("L'utente con id: " + id + " non è stato trovato!"));
    }

    public Utente findByUsername(String username) {
        return this.utenteRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("L'utente con username: " + username + " non è stato trovato!"));
    }

    public boolean existsByUsername(String username) {
        return utenteRepository.findByUsername(username).isPresent();
    }

    public Utente registraUtente(RegistraUtenteDTO dto) {
        if (utenteRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Email già in uso!");
        }
        Utente u = new Utente();
        u.setNome(dto.nome());
        u.setCognome(dto.cognome());
        u.setEmail(dto.email());
        u.setUsername(dto.username());
        u.setPassword(passwordEncoder.encode(dto.password()));

        // Assegno di base user
        Ruolo userRole = ruoloRepo.findByNomeRuolo("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("Errore: Ruolo ROLE_USER non presente nel DB!"));
        Set<Ruolo> ruoli = new HashSet<>();
        ruoli.add(userRole);
        u.setRuoli(ruoli);

        return utenteRepository.save(u);
    }

    // 2. FUNZIONE AGGIUNTIVA PER AGGIUNGERE ADMIN
    public void aggiungiRuoloAdmin(UUID utenteId) {
        Utente u = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new NotFoundException("L'utente con id: " + utenteId + " non è stato trovato!"));

        Ruolo adminRole = ruoloRepo.findByNomeRuolo("ROLE_ADMIN")
                .orElseThrow(() -> new NotFoundException("Errore: Ruolo ROLE_ADMIN non presente nel DB!"));

        // Aggingo il ruolo al set esistente (così avrà sia USER che ADMIN)
        u.getRuoli().add(adminRole);

        utenteRepository.save(u);
    }
}


