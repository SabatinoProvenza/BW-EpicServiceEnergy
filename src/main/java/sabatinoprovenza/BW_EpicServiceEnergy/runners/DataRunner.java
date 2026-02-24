package sabatinoprovenza.BW_EpicServiceEnergy.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ComuneRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ProvinciaRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.service.DataImportService;

@Component
public class DataRunner implements CommandLineRunner {
    private final DataImportService dataImportService;
    private final ProvinciaRepository provinciaRepository;
    private final ComuneRepository comuneRepository;

    public DataRunner(DataImportService dataImportService, ProvinciaRepository provinciaRepository, ComuneRepository comuneRepository) {
        this.dataImportService = dataImportService;
        this.provinciaRepository = provinciaRepository;
        this.comuneRepository = comuneRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        dataImportService.importData();
        Long prov = provinciaRepository.count();
        Long com = comuneRepository.count();
        System.out.println("-----------------------------------------");
        System.out.println(prov);
        System.out.println(com);
    }
}
