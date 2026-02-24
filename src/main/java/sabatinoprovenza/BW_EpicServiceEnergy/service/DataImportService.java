package sabatinoprovenza.BW_EpicServiceEnergy.service;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Comune;
import sabatinoprovenza.BW_EpicServiceEnergy.entities.Provincia;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ComuneRepository;
import sabatinoprovenza.BW_EpicServiceEnergy.repositories.ProvinciaRepository;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class DataImportService {

    @Autowired
    private ProvinciaRepository provinciaRepo;

    @Autowired
    private ComuneRepository comuneRepo;

    private String norm(String s) {
        return s == null ? null : s.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    public void importData() throws Exception {
        // 1. CARICAMENTO PROVINCE
        Map<String, Provincia> provinceMap = new HashMap<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader("province-italiane.csv"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1) // Salta l'header
                .build()) {

            String[] values;
            while ((values = reader.readNext()) != null) {
                Provincia p = new Provincia();
                p.setSigla(values[0].trim().toUpperCase(Locale.ROOT));
                p.setNome(values[1].trim());
                p.setRegione(values[2].trim());

                provinciaRepo.save(p);
                String nomeNormalizzato = p.getNome().toLowerCase().replace("-", " ");
                provinceMap.put(nomeNormalizzato, p);
            }
        }

        // 2. CARICAMENTO COMUNI
        try (CSVReader reader = new CSVReaderBuilder(new FileReader("comuni-italiani.csv"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1)
                .build()) {

            String[] values;
            while ((values = reader.readNext()) != null) {
                String nomeComune = values[2].trim();


                String nomeProvincia = norm(values[3]);
                Provincia prov = provinceMap.get(nomeProvincia);

                if (prov != null) {
                    Comune c = new Comune();
                    c.setNome(nomeComune);
                    c.setProvincia(prov);
                    comuneRepo.save(c);
                } else {
                    System.err.println("Provincia non trovata per il comune: " + nomeComune);
                }
            }
        }
    }
}
