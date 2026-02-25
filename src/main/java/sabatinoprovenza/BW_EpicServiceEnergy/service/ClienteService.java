package sabatinoprovenza.BW_EpicServiceEnergy.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Cliente;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Comune;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Indirizzo;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.TipoCliente;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.BadRequestException;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.NotEmptyException;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.NotFoundException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.ClienteDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ClienteRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ComuneRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.IndirizzoRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private IndirizzoRepository indirizzoRepo;
    @Autowired
    private Cloudinary cloudinaryUploader;
    @Autowired
    private MailgunService mailgunService;

    @Autowired
    private ComuneRepository comuneRepo;

    public ClienteService(ClienteRepository clienteRepository, IndirizzoRepository indirizzoRepo, Cloudinary cloudinaryUploader, ComuneRepository comuneRepo) {
        this.clienteRepository = clienteRepository;
        this.indirizzoRepo = indirizzoRepo;
        this.cloudinaryUploader = cloudinaryUploader;
        this.comuneRepo = comuneRepo;
    }

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

        Cliente newCliente = clienteRepository.save(c);

        this.generaEmail(newCliente.getEmail());

        return newCliente;
        
    }

    private void generaEmail(String email) {

        String subject = "Registrazione a EpicEnergyService avvenuta con successo!";
        String body = "Benvenuto in EpicEnergyService, per ulteriori informazioni sulle nostre offerte non esitare a contattarci!";

        try {
            mailgunService.sendSimpleMessage(email, subject, body);
            System.out.println("Email inviata con successo a " + email);
        } catch (UnirestException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
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

    // 3. UPLOAD DEL LOGO DEL CLIENTE

    public Cliente findByIdAndUpload(UUID clienteId, MultipartFile file) {
        if (file.isEmpty()) throw new NotEmptyException();
        Cliente found = clienteRepository.findById(clienteId).orElseThrow(() -> new NotFoundException("Il cliente con id: " + clienteId + " non è stato trovato!"));
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl = (String) result.get("secure_url");

            found.setLogoAzienda(imageUrl);

            return clienteRepository.save(found);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // Metodo particolare per ricerca dinamica, utilizza le JPA Specifications per costruire
    // una query SQL su misura in base ai parametri che arrivano (o non arrivano) dal frontend/Postman.
    public Page<Cliente> ricercaAvanzataClienti(
            Double fatturato,
            LocalDate dataInserimento,
            LocalDate dataUltimoContatto,
            String parteDelNome,
            Pageable pageable) {

        // 1. SPECIFICATION.WHERE: Inizializza il contenitore della query.
        // Si usa una Lambda Expression (root, query, cb).
        // root  -> Rappresenta l'entità 'Cliente' (da quale tabella pesco i dati)
        // query -> Rappresenta la struttura della query (ordinamenti, raggruppamenti - qui usata implicitamente)
        // cb    -> CriteriaBuilder: la "fabbrica" di operatori logici (uguale, maggiore, like, ecc.)
        Specification<Cliente> spec = Specification.where((root, query, cb) ->
                // Parto con una condizione base sempre vera per i nostri scopi: isEnable deve essere true
                cb.equal(root.get("isEnable"), true)
        );

        // 2. AND DINAMICI: Per ogni parametro, controllo se è nullo.
        // Se l'utente non ha passato il filtro su Postman, semplicemente non aggiungo il mattoncino alla query.

        if (fatturato != null) {
            // .and() concatena una nuova condizione alla precedente usando la logica SQL 'AND'
            spec = spec.and((root, query, cb) ->
                    // cb.greaterThanOrEqualTo trasforma il codice in SQL: "WHERE fatturato_annuale >= ?"
                    cb.greaterThanOrEqualTo(root.get("fatturatoAnnuale"), fatturato)
            );
        }

        if (dataInserimento != null) {
            spec = spec.and((root, query, cb) ->
                    // root.get("campo") serve per puntare esattamente al nome della variabile nell'Entity Java
                    cb.equal(root.get("dataInserimento"), dataInserimento)
            );
        }

        if (dataUltimoContatto != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("dataUltimoContatto"), dataUltimoContatto)
            );
        }

        if (parteDelNome != null) {
            spec = spec.and((root, query, cb) ->
                    // Gestisco il "Case Insensitive":
                    // cb.lower(root.get(...)) trasforma il dato nel DB in minuscolo
                    // .toLowerCase() trasforma la stringa cercata in minuscolo
                    // cb.like gestisce la ricerca parziale: "%" + valore + "%" significa "contiene"
                    cb.like(cb.lower(root.get("ragioneSociale")), "%" + parteDelNome.toLowerCase() + "%")
            );
        }

        // 3. ESECUZIONE: Passo la Specification 'montata' e il Pageable (per paginazione e ordinamento)
        return clienteRepository.findAll(spec, pageable);
    }
}
