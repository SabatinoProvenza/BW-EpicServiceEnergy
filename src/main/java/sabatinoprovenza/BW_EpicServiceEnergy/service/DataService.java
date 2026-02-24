package sabatinoprovenza.BW_EpicServiceEnergy.service;

//@Service
//@RequiredArgsConstructor
//public class DataService<T, K> {
//    final CSVMapper<T, K> csvMapper;
//
//    final public Set<T> parseCSV(InputStream stream, Class<K> clazz) throws IOException {
//
//        try (Reader reader = new BufferedReader(new InputStreamReader(stream))) {
//            // creating the strategy object
//            HeaderColumnNameMappingStrategy<K> strategy = new HeaderColumnNameMappingStrategy<>();
//            // setting the format of the data representation in the header
//            strategy.setType(clazz);
//            // Creating instance of CSVTOBEAN class responsable of the mapping
//            CsvToBean<K> csvToBean = new CsvToBeanBuilder<K>(reader)
//                    // Setting the startegy
//                    .withMappingStrategy(strategy)
//                    // Ignore empty lines and leading spaces
//                    .withIgnoreEmptyLine(true)
//                    .withIgnoreLeadingWhiteSpace(true)
//                    .build();
//            // parse the data into the Objects with type T
//            return csvToBean.parse()
//                    .stream()
//                    .map(csvMapper::mapTo)
//                    .collect(Collectors.toSet());
//        }
//    }
//}
