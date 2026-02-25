package sabatinoprovenza.BW_EpicServiceEnergy.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Cliente;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.ValidationException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.ClienteDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/clienti")
public class ClienteController {
    private ClienteService clienteService;

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
}
