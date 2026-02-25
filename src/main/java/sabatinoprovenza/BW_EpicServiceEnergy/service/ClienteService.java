package sabatinoprovenza.BW_EpicServiceEnergy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Cliente;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Comune;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Indirizzo;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.TipoCliente;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.BadRequestException;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.NotFoundException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.ClienteDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ClienteRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ComuneRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.IndirizzoRepository;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private IndirizzoRepository indirizzoRepo;

    @Autowired
    private ComuneRepository comuneRepo;

    public Cliente salvaCliente(ClienteDTO dto) {
        if (clienteRepository.findByEmail(dto.email()).isPresent()) {
            throw new BadRequestException("Email già in uso!");
        }
        // 1. Gestione Sede Legale (Obbligatoria)
        Indirizzo sedeLegale = creaIndirizzo(
                dto.viaLegale(),
                dto.civicoLegale(),
                dto.capLegale(),
                dto.nomeComuneLegale()
        );

        // 2. Gestione Sede Operativa (Opzionale)
        Indirizzo sedeOperativa = null;
        // se presente la carico altrimenti la lascio null
        if (dto.viaOperativa() != null && !dto.viaOperativa().isBlank()) {
            sedeOperativa = creaIndirizzo(
                    dto.viaOperativa(),
                    dto.civicoOperativa(),
                    dto.capOperativa(),
                    dto.nomeComuneOperativa()
            );
        }

        // 3. Creazione Cliente
        Cliente c = new Cliente();
        c.setRagioneSociale(dto.ragioneSociale());
        c.setPartitaIva(dto.partitaIva());
        c.setEmail(dto.email());
        c.setFatturatoAnnuale(dto.fatturatoAnnuale());
        c.setPec(dto.pec());
        c.setTelefono(dto.telefono());
        c.setEmailContatto(dto.emailContatto());
        c.setNomeContatto(dto.nomeContatto());
        c.setCognomeContatto(dto.cognomeContatto());
        c.setTelefonoContatto(dto.telefonoContatto());
        c.setTipoCliente(TipoCliente.valueOf(dto.tipoCliente().toUpperCase()));
        c.setLogoAzienda("https://ui-avatars.com/api/?name=" + dto.nomeContatto() + "+" + dto.cognomeContatto());
        c.setDataInserimento(LocalDate.now());
        c.setSedeLegale(sedeLegale);
        c.setSedeOperativa(sedeOperativa);

        return clienteRepository.save(c);
    }

    // Metodo privato per non ripetere il codice della creazione degli indirizzi
    private Indirizzo creaIndirizzo(String via, String civico, String cap, String nomeComune) {
        Indirizzo ind = new Indirizzo();
        ind.setVia(via);
        ind.setCivico(civico);
        ind.setCap(cap);

        Comune com = comuneRepo.findByNome(nomeComune)
                .orElseThrow(() -> new NotFoundException("Comune non trovato: " + nomeComune));
        ind.setComune(com);

        return indirizzoRepo.save(ind);
    }

    public void softDelete(UUID id) {
        // 1. Cerco il cliente
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Impossibile eliminare: Cliente non trovato!"));

        // 2. Imposto isEnable a false
        cliente.setEnable(false);

        // 3. Salvo la modifica
        clienteRepository.save(cliente);
    }
}
