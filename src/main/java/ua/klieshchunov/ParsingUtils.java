package ua.klieshchunov;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import ua.klieshchunov.entity.LawViolation;
import ua.klieshchunov.entity.LawViolationsWrapper;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParsingUtils {
    public static void generateLawViolationStatistics(String directoryPath, String statisticsFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();
        File[] lawViolationFiles = getArrayOfFiles(directoryPath);
        final Executor executor = Executors.newFixedThreadPool(15);

        List<CompletableFuture<Map<String, Double>>> futures = getFutureStatistics(mapper, lawViolationFiles, executor);
        joinAllFutures(futures);

        Map<String,Double> result = concatStatistics(futures);
        result = sortByFineAmount(result);
        writeToXml(xmlMapper, statisticsFilePath, result);
    }

    private static File[] getArrayOfFiles(String directoryPath) {
        File violationsDirectory = new File(directoryPath);
        return violationsDirectory.listFiles(path -> {
            Pattern pattern = Pattern.compile("^violations_\\d{4}$");
            return pattern.matcher(path.getName()).matches();
        });
    }

    private static List<CompletableFuture<Map<String, Double>>> getFutureStatistics(ObjectMapper mapper, File[] lawViolationFiles, Executor executor) {
        return Arrays.stream(lawViolationFiles)
                .map(file -> CompletableFuture.supplyAsync(
                        () -> {
                            DelayUtils.delay(50);
                            List<LawViolation> lawViolations = readObjectFromFileToList(mapper, file);
                            return createStatistics(lawViolations);
                        }, executor))
                .toList();
    }

    private static void joinAllFutures(List<CompletableFuture<Map<String, Double>>> futures) {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
    }

    private static List<LawViolation> readObjectFromFileToList(ObjectMapper mapper, File lawViolationFile) {
        List<LawViolation> lawViolationList = new ArrayList<>();

        try (InputStream is = new FileInputStream(lawViolationFile.getPath())) {
            List<LawViolation> partialLawViolationList = mapper.readValue(is.readAllBytes(), new TypeReference<>() {});
            lawViolationList.addAll(partialLawViolationList);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't convert json to the LawViolation objects list", e);
        }

        return lawViolationList;
    }


    private static Map<String, Double> createStatistics(List<LawViolation> lawViolations) {

        return lawViolations.stream()
                .collect(Collectors.groupingBy(
                        LawViolation::getType,
                        Collectors.summingDouble(LawViolation::getFineAmount))
                );
    }

    private static Map<String,Double> concatStatistics(List<CompletableFuture<Map<String, Double>>> statsFutures) {
        return statsFutures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        return Collections.emptyMap();
                    }
                })
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy((map) -> (String)map.getKey(),
                        Collectors.summingDouble(map -> (double)map.getValue())));
    }

    private static Map<String, Double> sortByFineAmount(Map<String,Double> map) {
        Map<String, Double> copiedMap = new HashMap<>(map);
        return copiedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal, LinkedHashMap::new));
    }

    private static void writeToXml(XmlMapper xmlMapper, String statisticsFilePath, Map<String, Double> resultingMap) {
        try {
            File statisticsFile = new File(statisticsFilePath);
            statisticsFile.createNewFile();
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(statisticsFile, new LawViolationsWrapper(resultingMap));
        } catch (IOException e) {
            throw new RuntimeException("Error writing results to the file", e);
        }
    }
}
