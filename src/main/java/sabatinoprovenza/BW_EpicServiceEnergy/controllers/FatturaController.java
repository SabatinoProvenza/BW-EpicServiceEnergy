package sabatinoprovenza.BW_EpicServiceEnergy.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Fattura;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.ValidationException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.FatturaDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.service.FatturaService;

import java.util.List;

@RestController
@RequestMapping("/fatture")
public class FatturaController {

	private final FatturaService fatturaService;

	@Autowired
	public FatturaController(FatturaService fatturaService) {
		this.fatturaService = fatturaService;
	}

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

}
