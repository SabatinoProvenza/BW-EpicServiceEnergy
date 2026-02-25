package sabatinoprovenza.BW_EpicServiceEnergy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Cliente;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Fattura;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.StatoFattura;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.NotFoundException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.FatturaDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ClienteRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.FatturaRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.StatoFatturaRepository;

@Service
public class FatturaService {

	@Autowired
	private FatturaRepository fatturaRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private StatoFatturaRepository statoFatturaRepository;

	public Fattura salvaFattura(FatturaDTO dto) {
		Cliente cliente = clienteRepository.findById(dto.clienteId())
				.orElseThrow(() -> new NotFoundException("Cliente con id " + dto.clienteId() + " non trovato!"));

		Fattura f = new Fattura();
		f.setData(dto.data());
		f.setImporto(dto.importo());
		f.setNumero(dto.numero());
		f.setCliente(cliente);

		if (dto.statoFatturaId() != null) {
			StatoFattura stato = statoFatturaRepository.findById(dto.statoFatturaId())
					.orElseThrow(() -> new NotFoundException("Stato fattura con id " + dto.statoFatturaId() + " non trovato!"));
			f.setStatoFattura(stato);
		}

		return fatturaRepository.save(f);
	}
}
