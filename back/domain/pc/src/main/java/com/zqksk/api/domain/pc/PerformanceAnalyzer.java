package com.zqksk.api.domain.pc;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class PerformanceAnalyzer {
    private final CpuFinder cpuFinder;
    private final List<MemoryPerformance> memoryPerformances;

    public PerformanceAnalyzer(CpuFinder cpuFinder) {
        this.cpuFinder = cpuFinder;
        this.memoryPerformances = new ArrayList<>(Arrays.asList(
                new MemoryPerformance("2GB", 2, 0.1),
                new MemoryPerformance("3GB", 3, 0.5),
                new MemoryPerformance("4GB", 4, 0.5),
                new MemoryPerformance("5GB", 5, 0.8),
                new MemoryPerformance("6GB", 6, 0.8),
                new MemoryPerformance("7GB", 7, 1.0),
                new MemoryPerformance("8GB", 8, 1.0),
                new MemoryPerformance("9GB", 9, 1.2),
                new MemoryPerformance("10GB", 10, 1.2),
                new MemoryPerformance("11GB", 11, 1.3),
                new MemoryPerformance("12GB", 12, 1.3),
                new MemoryPerformance("13GB", 13, 1.4),
                new MemoryPerformance("14GB", 14, 1.4),
                new MemoryPerformance("15GB", 15, 1.5),
                new MemoryPerformance("16GB", 16, 1.5),
                new MemoryPerformance("17GB", 17, 1.6),
                new MemoryPerformance("18GB", 18, 1.6),
                new MemoryPerformance("19GB", 19, 1.6),
                new MemoryPerformance("20GB", 20, 1.6),
                new MemoryPerformance("21GB", 21, 1.7),
                new MemoryPerformance("22GB", 22, 1.7),
                new MemoryPerformance("23GB", 23, 1.7),
                new MemoryPerformance("24GB", 24, 1.7),
                new MemoryPerformance("25GB", 25, 1.8),
                new MemoryPerformance("26GB", 26, 1.8),
                new MemoryPerformance("27GB", 27, 1.8),
                new MemoryPerformance("28GB", 28, 1.8),
                new MemoryPerformance("29GB", 29, 1.9),
                new MemoryPerformance("30GB", 30, 1.9),
                new MemoryPerformance("31GB", 31, 2.0),
                new MemoryPerformance("32GB", 32, 2.0)
        ));
    }

    public CpuMemoryScore analyzeSingleInstallInfo(InstallInfoData data) {
        CpuScoreResult cpuScoreResult = getCpuScore(data);
        MemoryScoreResult memoryScoreResult = getMemoryScore(data);

        int totalScore = (int) Math.round(cpuScoreResult.cpuScore() * memoryScoreResult.memoryScore());

        return new CpuMemoryScore(
                data.id(),
                cpuScoreResult.cpuName(),
                cpuScoreResult.cpuModel(),
                memoryScoreResult.memory(),
                cpuScoreResult.cpuScore(),
                memoryScoreResult.memoryScore(),
                totalScore,
                1
        );
    }

    public List<CpuMemoryScore> analyzeInstallInfo(List<InstallInfoData> rawDatas) {
        if (rawDatas.size() == 1) {
            return List.of(analyzeSingleInstallInfo(rawDatas.get(0)));
        }

        List<CpuMemoryScore> cpuMemoryScores = rawDatas.stream()
                .map(data -> {
                    CpuScoreResult cpuScoreResult = getCpuScore(data);
                    MemoryScoreResult memoryScoreResult = getMemoryScore(data);

                    int totalScore = (int) Math.round(cpuScoreResult.cpuScore() * memoryScoreResult.memoryScore());

                    return new CpuMemoryScore(
                            data.id(),
                            cpuScoreResult.cpuName(),
                            cpuScoreResult.cpuModel(),
                            memoryScoreResult.memory(),
                            cpuScoreResult.cpuScore(),
                            memoryScoreResult.memoryScore(),
                            totalScore,
                            0
                    );
                })
                .sorted(Comparator.comparingDouble(CpuMemoryScore::totalScore))
                .toList();

        Map<String, Long> unitCounts = cpuMemoryScores.stream()
                .collect(Collectors.groupingBy(
                        score -> score.cpuModel() + " " + score.memory(),
                        Collectors.counting()
                ));

        return cpuMemoryScores.stream()
                .collect(Collectors.groupingBy(
                        score -> score.cpuModel() + " " + score.memory(),
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            CpuMemoryScore first = list.get(0);
                            String key = first.cpuModel() + " " + first.memory();
                            return new CpuMemoryScore(
                                    first.id(),
                                    first.cpuName(),
                                    first.cpuModel(),
                                    first.memory(),
                                    first.cpuScore(),
                                    first.memoryScore(),
                                    first.totalScore(),
                                    unitCounts.get(key).intValue()
                            );
                        })
                ))
                .values()
                .stream()
                .toList();
    }

    public CpuScoreResult getCpuScore(InstallInfoData data) {
        String cpuName = data.cpu().toUpperCase();
        String brand = Brand.from(cpuName).name();

        List<CpuPerformance> cpuList = cpuFinder.getCpusByBrand(brand);
        CpuPerformance bestMatch = findBestMatch(cpuName, cpuList);

        if (bestMatch != null) {
            return new CpuScoreResult(cpuName, bestMatch.name(), bestMatch.score());
        }

        return new CpuScoreResult(cpuName, "미지정", 0);
    }

//    private String determineBrand(String cpuName) {
//        if (cpuName.contains("INTEL")) {
//            return "INTEL";
//        } else if (cpuName.contains("AMD")) {
//            return "AMD";
//        } else if (cpuName.contains("APPLE")) {
//            return "APPLE";
//        }else if (cpuName.contains("ARM")) {
//            return "ARM";
//        }else if (cpuName.contains("QUALCOMM")) {
//            return "QUALCOMM";
//        }else if (cpuName.contains("MEDIATEK")) {
//            return "MEDIATEK";
//        }else if (cpuName.contains("SAMSUNG")) {
//            return "SAMSUNG";
//        }else if (cpuName.contains("VIA")) {
//            return "VIA";
//        }
//        return "UNKNOWN";
//    }

    private CpuPerformance findBestMatch(String cpuName, List<CpuPerformance> cpuList) {
        if (cpuList.isEmpty()) {
            return null;
        }

        String[] cpuNameWords = cpuName.split(" ");
        CpuPerformance bestMatch = null;
        int maxMatchCount = 0;

        for (CpuPerformance cpu : cpuList) {
            String[] cpuModelWords = cpu.name().toUpperCase().split(" ");
            int matchCount = countMatches(cpuNameWords, cpuModelWords);

            if (matchCount > maxMatchCount) {
                maxMatchCount = matchCount;
                bestMatch = cpu;
            }
        }

        return bestMatch;
    }

    private int countMatches(String[] cpuNameWords, String[] cpuModelWords) {
        int matches = 0;
        for (String nameWord : cpuNameWords) {
            for (String modelWord : cpuModelWords) {

                modelWord = modelWord.replaceAll("\\(R\\)|\\(TM\\)", "");

                if (nameWord.equalsIgnoreCase(modelWord) || modelWord.equalsIgnoreCase(nameWord)) {
                    matches++;
                }
            }
        }
        return matches;
    }

    public MemoryScoreResult getMemoryScore(InstallInfoData data) {
        String memoryName = data.memory();
        double memoryScore = 0;

        if (memoryName != null && !memoryName.isEmpty()) {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(memoryName);
            if (matcher.find()) {
                int capacity = Integer.parseInt(matcher.group());

                Optional<MemoryPerformance> memoryPerformance = memoryPerformances.stream()
                        .filter(item -> capacity == item.capacity())
                        .findFirst();

                if (memoryPerformance.isPresent()) {
                    memoryScore = memoryPerformance.get().memoryCoefficient();
                } else {
                    MemoryPerformance maxMemory = memoryPerformances.stream()
                            .max(Comparator.comparingInt(MemoryPerformance::capacity))
                            .orElseThrow();

                    if (capacity > maxMemory.capacity()) {
                        memoryScore = maxMemory.memoryCoefficient();
                    }
                }
            }
        }

        return new MemoryScoreResult(memoryName, memoryScore);
    }
}