package sabatinoprovenza.BW_EpicServiceEnergy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Utente;
import sabatinoprovenza.BW_EpicServiceEnergy.service.UtenteService;

import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    @Autowired
    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    // 1. DELETE /utenti/{id}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUtente(@PathVariable UUID id) {
        this.utenteService.deleteUtente(id);
    }

    // 2. PATCH /123/avatar

    @PatchMapping("/{utenteId}/avatar")
    public Utente uploadImage(@RequestParam("profile_picture") MultipartFile file, @PathVariable UUID utenteId) {

        Utente utenteModified = this.utenteService.findByIdAndUploadAvatar(utenteId, file);

        return utenteModified;
    }

}
