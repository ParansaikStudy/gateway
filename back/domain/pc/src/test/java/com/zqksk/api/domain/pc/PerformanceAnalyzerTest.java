package com.zqksk.api.domain.pc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PerformanceAnalyzerTest {
    @Mock
    private CpuFinder cpuFinder;

    @InjectMocks
    private PerformanceAnalyzer performanceAnalyzer;

    @BeforeEach
    void setUp() {
        // Intel CPU 목록
        List<CpuPerformance> intelCpus = Arrays.asList(
                new CpuPerformance("INTEL", "Intel Core i3-9100HL @ 1.60GHz", 3065),
                new CpuPerformance("INTEL", "Intel Core i5-10210Y @ 1.00GHz", 4475),
                new CpuPerformance("INTEL", "Intel Core i3-9100TE @ 2.20GHz", 4791),
                new CpuPerformance("INTEL", "Intel Core i3-9100T @ 3.10GHz", 5472),
                new CpuPerformance("INTEL", "Intel Core i5-1030NG7 @ 1.10GHz", 5695),
                new CpuPerformance("INTEL", "Intel Core i3-9300T @ 3.20GHz", 6108),
                new CpuPerformance("INTEL", "Intel Core i5-10210U @ 1.60GHz", 6203),
                new CpuPerformance("INTEL", "Intel Core i5-10310U @ 1.70GHz", 6451),
                new CpuPerformance("INTEL", "Intel Core i3-9100 @ 3.60GHz", 6626),
                new CpuPerformance("INTEL", "Intel Core i3-9100F @ 3.60GHz", 6734),
                new CpuPerformance("INTEL", "Intel Core i3-9100E @ 3.10GHz", 6868),
                new CpuPerformance("INTEL", "Intel Core i3-9300 @ 3.70GHz", 7137),
                new CpuPerformance("INTEL", "Intel Core i5-1035G1 @ 1.00GHz", 7357),
                new CpuPerformance("INTEL", "Intel Core i3-9320 @ 3.70GHz", 7358),
                new CpuPerformance("INTEL", "Intel Core i3-9350KF @ 4.00GHz", 7549),
                new CpuPerformance("INTEL", "Intel Core i3-9350K @ 4.00GHz", 7700),
                new CpuPerformance("INTEL", "Intel Core i5-8500T @ 2.10GHz", 7736),
                new CpuPerformance("INTEL", "Intel Core i5-1035G4 @ 1.10GHz", 7858),
                new CpuPerformance("INTEL", "Intel Core i5-10200H @ 2.40GHz", 8091),
                new CpuPerformance("INTEL", "Intel Core i5-10300H @ 2.50GHz", 8351),
                new CpuPerformance("INTEL", "Intel Core i5-10500TE @ 2.30GHz", 8449),
                new CpuPerformance("INTEL", "Intel Core i5-8500B @ 3.00GHz", 8920),
                new CpuPerformance("INTEL", "Intel Core i5-8500 @ 3.00GHz", 9545),
                new CpuPerformance("INTEL", "Intel Core i5-8600 @ 3.10GHz", 9950),
                new CpuPerformance("INTEL", "Intel Core i5-10500T @ 2.30GHz", 10098),
                new CpuPerformance("INTEL", "Intel Core i5-10500E @ 3.10GHz", 10827),
                new CpuPerformance("INTEL", "Intel Core i5-10500H @ 2.50GHz", 11231),
                new CpuPerformance("INTEL", "Intel Core i5-10500 @ 3.10GHz", 13406)
        );

        // AMD CPU 목록
        List<CpuPerformance> amdCpus = Arrays.asList(
                new CpuPerformance("AMD", "AMD Athlon 2650e", 274),
                new CpuPerformance("AMD", "AMD Athlon 2800+", 291),
                new CpuPerformance("AMD", "AMD Athlon 2850e", 333),
                new CpuPerformance("AMD", "AMD Athlon 1640B", 412),
                new CpuPerformance("AMD", "AMD Athlon 200GE", 4115),
                new CpuPerformance("AMD", "AMD Athlon 220GE", 4432),
                new CpuPerformance("AMD", "AMD Athlon 240GE", 4527)
        );

        // Mock 설정
        when(cpuFinder.getCpus()).thenReturn(
                Stream.concat(intelCpus.stream(), amdCpus.stream())
                        .toList()
        );

        when(cpuFinder.getCpusByBrand("INTEL")).thenReturn(intelCpus);
        when(cpuFinder.getCpusByBrand("AMD")).thenReturn(amdCpus);
        when(cpuFinder.getCpusByBrand("UNKNOWN")).thenReturn(Collections.emptyList());
    }

    @Test
    @DisplayName("단일 PC 성능 분석 테스트")
    void analyzeSingleInstallInfo() {
        // given
        InstallInfoData data = new InstallInfoData(1L, "Intel(R) Core(TM) i5-8500 CPU @ 3.00GHz", "16 GB");

        // when
        CpuMemoryScore result = performanceAnalyzer.analyzeSingleInstallInfo(data);

        // then
        System.out.println("\n=== 단일 PC 분석 결과 ===");
        System.out.printf("CPU: %s%n", result.cpuName());
        System.out.printf("CPU Model: %s%n", result.cpuModel());
        System.out.printf("Memory: %s%n", result.memory());
        System.out.printf("CPU Score: %.2f%n", result.cpuScore());
        System.out.printf("Memory Score: %.2f%n", result.memoryScore());
        System.out.printf("Total Score: %d%n", result.totalScore());
        System.out.printf("Unit Count: %d%n", result.unitCount());

        assertNotNull(result);
    }

    @Test
    @DisplayName("여러 PC의 성능 분석 테스트")
    void analyzeMultipleInstallInfo() {
        // given
        List<InstallInfoData> testData = Arrays.asList(
                new InstallInfoData(1L, "Intel(R) Core(TM) i5-8500 CPU @ 3.00GHz", "16 GB"),
                new InstallInfoData(2L, "Intel(R) Core(TM) i3-9100F CPU @ 3.60GHz", "8 GB"),
                new InstallInfoData(3L, "Intel(R) Core(TM) i5-10500E CPU @ 3.10GHz", "16 GB"),
                new InstallInfoData(4L, "AMD Athlon 200GE with Radeon Vega Graphics", "16 GB")
        );

        // when
        List<CpuMemoryScore> results = performanceAnalyzer.analyzeInstallInfo(testData);

        // then
        System.out.println("\n=== 다중 PC 분석 결과 ===");
        System.out.println("ID    CPU Name     CPU Model                Memory    CPU Score    Memory Score    Total Score    Unit Count");
        System.out.println("----------------------------------------------------------------------------------------");
        results.forEach(result ->
                System.out.printf("%-5d %-24s %-24s %-9s %-12.2f %-14.2f %d %d%n",
                        result.id(),
                        result.cpuName(),
                        result.cpuModel(),
                        result.memory(),
                        result.cpuScore(),
                        result.memoryScore(),
                        result.totalScore(),
                        result.unitCount()
                )
        );

        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    @DisplayName("브랜드 판별 테스트")
    void testDetermineBrand() {
        // given
        InstallInfoData intelData = new InstallInfoData(1L, "Intel(R) Core(TM) i5-8500 CPU @ 3.00GHz", "16 GB");
        InstallInfoData amdData = new InstallInfoData(2L, "AMD Athlon 200GE", "16 GB");
        InstallInfoData unknownData = new InstallInfoData(3L, "Unknown CPU Model", "16 GB");

        // when & then
        CpuScoreResult intelResult = performanceAnalyzer.getCpuScore(intelData);
        CpuScoreResult amdResult = performanceAnalyzer.getCpuScore(amdData);
        CpuScoreResult unknownResult = performanceAnalyzer.getCpuScore(unknownData);

        System.out.println("\n=== 브랜드 판별 테스트 결과 ===");
        System.out.printf("Intel CPU Score: %.2f (Model: %s)%n", intelResult.cpuScore(), intelResult.cpuModel());
        System.out.printf("AMD CPU Score: %.2f (Model: %s)%n", amdResult.cpuScore(), amdResult.cpuModel());
        System.out.printf("Unknown CPU Score: %.2f (Model: %s)%n", unknownResult.cpuScore(), unknownResult.cpuModel());

        assertAll(
                () -> assertNotEquals("미지정", intelResult.cpuModel()),
                () -> assertNotEquals("미지정", amdResult.cpuModel()),
                () -> assertEquals("미지정", unknownResult.cpuModel())
        );
    }
}