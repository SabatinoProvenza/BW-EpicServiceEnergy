package sabatinoprovenza.BW_EpicServiceEnergy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Cliente;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Fattura;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.StatoFattura;
import sabatinoprovenza.BW_EpicServiceEnergy.exceptions.NotFoundException;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.FatturaDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ClienteRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.FatturaRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.StatoFatturaRepository;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class FatturaService {

    @Autowired
    private FatturaRepository fatturaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private StatoFatturaRepository statoFatturaRepository;

    public Fattura aggiornaStatoFattura(UUID fatturaId, UUID statoFatturaId) {
        Fattura f = fatturaRepository.findById(fatturaId)
                .orElseThrow(() -> new NotFoundException("Fattura con id " + fatturaId + " non trovata!"));
        StatoFattura stato = statoFatturaRepository.findById(statoFatturaId)
                .orElseThrow(() -> new NotFoundException("Stato fattura con id " + statoFatturaId + " non trovato!"));
        f.setStatoFattura(stato);
        return fatturaRepository.save(f);
    }

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
        } else {
            StatoFattura inAttesa = statoFatturaRepository.findByNomeStato("IN ATTESA")
                    .orElseThrow(() -> new NotFoundException("Stato 'IN ATTESA' non trovato nel DB!"));
            f.setStatoFattura(inAttesa);
        }

        return fatturaRepository.save(f);
    }

    public Page<Fattura> ricercaAvanzataFatture(
            UUID clienteId,
            UUID statoId,
            LocalDate data,
            Integer anno,
            Double minImporto,
            Double maxImporto,
            Pageable pageable) {

        // 1. Inizializzo la Specification (senza condizioni base obbligatorie in questo caso)
        Specification<Fattura> spec = Specification.where((root, query, cb) -> cb.conjunction());

        // 2. FILTRO PER CLIENTE (Uso l'ID del cliente)
        if (clienteId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("cliente").get("id"), clienteId)
            );
        }

        // 3. FILTRO PER STATO (Uso l'ID dello stato)
        if (statoId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("statoFattura").get("id"), statoId)
            );
        }

        // 4. FILTRO PER DATA ESATTA
        if (data != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("data"), data)
            );
        }

        // 5. FILTRO PER ANNO
        // Uso cb.function per chiamare la funzione SQL "year" sulla colonna "data"
        if (anno != null) {
            LocalDate inizioAnno = LocalDate.of(anno, 1, 1);
            LocalDate fineAnno = LocalDate.of(anno, 12, 31);
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("data"), inizioAnno, fineAnno)
            );
        }

        // 6. RANGE DI IMPORTI (Tra Min e Max)
        if (minImporto != null && maxImporto != null) {
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("importo"), minImporto, maxImporto)
            );
        } else if (minImporto != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("importo"), minImporto));
        } else if (maxImporto != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("importo"), maxImporto));
        }

        return fatturaRepository.findAll(spec, pageable);
    }
}
