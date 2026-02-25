package sabatinoprovenza.BW_EpicServiceEnergy.runners;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Utente;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.RegistraUtenteDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.StatoFattura;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ComuneRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ProvinciaRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.StatoFatturaRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.service.DataImportService;
import sabatinoprovenza.BW_EpicServiceEnergy.service.UtenteService;

@Component
public class DataRunner implements CommandLineRunner {
    private final DataImportService dataImportService;
    private final ProvinciaRepository provinciaRepository;
    private final ComuneRepository comuneRepository;
    private final UtenteService utenteService;
    private final StatoFatturaRepository statoFatturaRepository;
    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${ADMIN_EMAIL}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Value("${ADMIN_NOME}")
    private String adminNome;

    @Value("${ADMIN_COGNOME}")
    private String adminCognome;

    public DataRunner(DataImportService dataImportService, ProvinciaRepository provinciaRepository, ComuneRepository comuneRepository, UtenteService utenteService, StatoFatturaRepository statoFatturaRepository) {
        this.dataImportService = dataImportService;
        this.provinciaRepository = provinciaRepository;
        this.comuneRepository = comuneRepository;
        this.utenteService = utenteService;
        this.statoFatturaRepository = statoFatturaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        dataImportService.importData();
        if (statoFatturaRepository.count() == 0) {
            StatoFattura inAttesa = new StatoFattura();
            inAttesa.setNomeStato("IN ATTESA");
            statoFatturaRepository.save(inAttesa);

            StatoFattura pagata = new StatoFattura();
            pagata.setNomeStato("PAGATA");
            statoFatturaRepository.save(pagata);

            StatoFattura nonPagata = new StatoFattura();
            nonPagata.setNomeStato("NON PAGATA");
            statoFatturaRepository.save(nonPagata);

            System.out.println("Stati fattura inizializzati!");
        }
        if (!utenteService.existsByUsername(adminUsername)) {

            // Uso le variabili caricate dalle ENV
            RegistraUtenteDTO adminDto = new RegistraUtenteDTO(
                    adminUsername,
                    adminEmail,
                    adminPassword,
                    adminNome,
                    adminCognome
            );

            Utente creato = utenteService.registraUtente(adminDto);
            utenteService.aggiungiRuoloAdmin(creato.getId());

            System.out.println("ADMIN creato dalle variabili d'ambiente!");
        }


        Long prov = provinciaRepository.count();
        Long com = comuneRepository.count();
        System.out.println("-----------------------------------------");
        System.out.println(prov);
        System.out.println(com);
    }
}
