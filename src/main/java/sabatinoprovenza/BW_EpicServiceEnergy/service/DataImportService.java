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
import java.util.Map;

@Service
public class DataImportService {

    @Autowired
    private ProvinciaRepository provinciaRepo;
    @Autowired
    private ComuneRepository comuneRepo;

    private String associaProvincia(String nomeProvinciaCsv) {
        if (nomeProvinciaCsv == null) return null;

        // Pulisco solo gli spazi bianchi all'inizio e alla fine
        String nome = nomeProvinciaCsv.trim();

        // Mappo i nomi del file COMUNI verso i nomi del file PROVINCE
        switch (nome) {
            case "Monza e della Brianza":
                return "Monza-Brianza";
            case "Verbano-Cusio-Ossola":
                return "Verbania";
            case "Valle d'Aosta/Vallée d'Aoste":
                return "Aosta";
            case "Reggio nell'Emilia":
                return "Reggio-Emilia";
            case "Reggio Calabria":
                return "Reggio-Calabria";
            case "Pesaro e Urbino":
                return "Pesaro-Urbino";
            case "Forlì-Cesena":
                return "Forli-Cesena";
            case "La Spezia":
                return "La-Spezia";
            case "Ascoli Piceno":
                return "Ascoli-Piceno";
            case "Vibo Valentia":
                return "Vibo-Valentia";
            case "Bolzano/Bozen":
                return "Bolzano";
            default:
                return nome;
        }
    }

    public void importData() throws Exception {
        Map<String, Provincia> provinceMap = new HashMap<>();

        // 1. CARICAMENTO PROVINCE
        try (CSVReader reader = new CSVReaderBuilder(new FileReader("province-italiane.csv"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1).build()) {
            String[] v;
            while ((v = reader.readNext()) != null) {
                Provincia p = new Provincia();
                p.setSigla(v[0].trim().toUpperCase());
                p.setNome(v[1].trim());
                p.setRegione(v[2].trim());
                provinciaRepo.save(p);
                // Salviamo nella mappa usando il nome originale in minuscolo
                provinceMap.put(p.getNome().toLowerCase(), p);
            }
        }

        // Aggiunta manuale Sud Sardegna (perché manca nel file province)
        if (!provinceMap.containsKey("sud sardegna")) {
            Provincia su = new Provincia();
            su.setSigla("SU");
            su.setNome("Sud Sardegna");
            su.setRegione("Sardegna");
            provinciaRepo.save(su);
            provinceMap.put("sud sardegna", su);
        }

        // 2. CARICAMENTO COMUNI
        try (CSVReader reader = new CSVReaderBuilder(new FileReader("comuni-italiani.csv"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1).build()) {
            String[] v;
            while ((v = reader.readNext()) != null) {
                // Leggiamo il nome dal CSV
                String nomeDalCsv = v[3];

                // Applichiamo l'associazione corretta basata sui log
                String nomeAssociato = associaProvincia(nomeDalCsv);

                // Cerchiamo nella mappa
                Provincia prov = provinceMap.get(nomeAssociato.toLowerCase());

                if (prov != null) {
                    Comune c = new Comune();
                    c.setNome(v[2].trim());
                    c.setProvincia(prov);
                    comuneRepo.save(c);
                } else {
                    System.err.println("Provincia non trovata: " + nomeDalCsv);
                }
            }
        }
    }
}