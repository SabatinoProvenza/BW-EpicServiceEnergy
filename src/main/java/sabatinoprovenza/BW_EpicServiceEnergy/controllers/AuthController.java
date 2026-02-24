package sabatinoprovenza.BW_EpicServiceEnergy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Utente;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.ValidationException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.LoginDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.LoginResponseDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.RegistraUtenteDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.service.AuthService;
import sabatinoprovenza.BW_EpicServiceEnergy.service.UtenteService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UtenteService utenteService;

    @Autowired
    public AuthController(AuthService authService, UtenteService utenteService) {
        this.authService = authService;
        this.utenteService = utenteService;
    }


    // 1. POST URL/auth/login

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }


    // 2. POST URL/auth/register

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente createUtente(@RequestBody @Validated RegistraUtenteDTO utenteDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.utenteService.saveUtente(utenteDTO);
        }
    }


}
