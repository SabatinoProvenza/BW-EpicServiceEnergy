package sabatinoprovenza.BW_EpicServiceEnergy.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Cliente;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.ValidationException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.ClienteDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.service.ClienteService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clienti")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER')")
    public Cliente creaCliente(@RequestBody @Validated ClienteDTO dto, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.clienteService.salvaCliente(dto);
        }

    }

    // PATCH /123/avatar

    @PatchMapping("/{clienteId}/logo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Cliente uploadImage(@RequestParam("profile_picture") MultipartFile file, @PathVariable UUID clienteId) {

        return this.clienteService.findByIdAndUpload(clienteId, file);
    }

    @GetMapping
    public Page<Cliente> getClienti(
            // Parametri per i FILTRI (tutti opzionali)
            @RequestParam(required = false) Double fatturato,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inserimento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ultimoContatto,
            @RequestParam(required = false) String nome,

            // Parametri per PAGINAZIONE e ORDINAMENTO
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ragioneSociale") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        // Costruisco l'oggetto Pageable con direzione e campo di ordinamento
        Sort sort = direction.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Chiamiamo il tuo metodo "ganzo" nel Service
        return clienteService.ricercaAvanzataClienti(fatturato, inserimento, ultimoContatto, nome, pageable);
    }
}
