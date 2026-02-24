package sabatinoprovenza.BW_EpicServiceEnergy.runners;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Ruolo;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Utente;
import sabatinoprovenza.BW_EpicServiceEnergy.payload.RegistraUtenteDTO;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ComuneRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ProvinciaRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.RuoloRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.service.DataImportService;
import sabatinoprovenza.BW_EpicServiceEnergy.service.UtenteService;

@Component
public class DataRunner implements CommandLineRunner {
    private final DataImportService dataImportService;
    private final ProvinciaRepository provinciaRepository;
    private final ComuneRepository comuneRepository;
    private final RuoloRepository ruoloRepository;
    private final UtenteService utenteService;
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

    public DataRunner(DataImportService dataImportService, ProvinciaRepository provinciaRepository, ComuneRepository comuneRepository, RuoloRepository ruoloRepository, UtenteService utenteService) {
        this.dataImportService = dataImportService;
        this.provinciaRepository = provinciaRepository;
        this.comuneRepository = comuneRepository;
        this.ruoloRepository = ruoloRepository;
        this.utenteService = utenteService;
    }

    @Override
    public void run(String... args) throws Exception {
        dataImportService.importData();
        if (ruoloRepository.count() == 0) {
            ruoloRepository.save(new Ruolo("ROLE_USER"));
            ruoloRepository.save(new Ruolo("ROLE_ADMIN"));
        }
        RegistraUtenteDTO adminDto = new RegistraUtenteDTO(
                adminUsername,
                adminEmail,
                adminPassword,
                adminNome,
                adminCognome
        );


        Utente creato = utenteService.registraUtente(adminDto);
        utenteService.aggiungiRuoloAdmin(creato.getId());

        System.out.println("Utente ADMIN creato con successo!");
        Long prov = provinciaRepository.count();
        Long com = comuneRepository.count();
        System.out.println("-----------------------------------------");
        System.out.println(prov);
        System.out.println(com);
    }
}
