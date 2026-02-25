package sabatinoprovenza.BW_EpicServiceEnergy.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Ruolo;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Utente;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.NotEmptyException;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.NotFoundException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.RegistraUtenteDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.RuoloRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.UtenteRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;
    private final Cloudinary cloudinaryUploader;
    @Autowired
    private RuoloRepository ruoloRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository, Cloudinary cloudinaryUploader) {
        this.utenteRepository = utenteRepository;
        this.cloudinaryUploader = cloudinaryUploader;
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
        u.setAvatar("https://ui-avatars.com/api/?name="
                + dto.nome() + "+" + dto.cognome());

        // Assegno di base user
        Ruolo userRole = ruoloRepo.findByNomeRuolo("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("Errore: Ruolo ROLE_USER non presente nel DB!"));
        Set<Ruolo> ruoli = new HashSet<>();
        ruoli.add(userRole);
        u.setRuoli(ruoli);

        return utenteRepository.save(u);
    }

    public void deleteUtente(UUID id) {
        Utente u = this.findById(id);
        this.utenteRepository.delete(u);
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

    // 3. UPLOAD DELL'AVATAR DELL'UTENTE

    public Utente findByIdAndUploadAvatar(UUID utenteId, MultipartFile file) {
        if (file.isEmpty()) throw new NotEmptyException();
        Utente found = this.findById(utenteId);
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl = (String) result.get("secure_url");

            found.setAvatar(imageUrl);

            return utenteRepository.save(found);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}


