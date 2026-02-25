package sabatinoprovenza.BW_EpicServiceEnergy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Fattura;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.ValidationException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.FatturaDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.service.FatturaService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fatture")
public class FatturaController {

    private final FatturaService fatturaService;

    @Autowired
    public FatturaController(FatturaService fatturaService) {
        this.fatturaService = fatturaService;
    }

    // PATCH /fatture/{id}/stato/{idStato}
    //   /fatture/124/stato/5678
    @PatchMapping("/{id}/stato/{statoFatturaId}") // Modificato qui
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Fattura aggiornaStato(
            @PathVariable UUID id,
            @PathVariable UUID statoFatturaId // Cambiato da @RequestParam a @PathVariable
    ) {
        return this.fatturaService.aggiornaStatoFattura(id, statoFatturaId);
    }

    // POST /fatture
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Fattura salvaFattura(@RequestBody @Validated FatturaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return this.fatturaService.salvaFattura(body);
    }


    @GetMapping
    public Page<Fattura> getFatture(
            @RequestParam(required = false) UUID clienteId,
            @RequestParam(required = false) UUID statoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Integer anno,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return fatturaService.ricercaAvanzataFatture(clienteId, statoId, data, anno, min, max, pageable);
    }
}

