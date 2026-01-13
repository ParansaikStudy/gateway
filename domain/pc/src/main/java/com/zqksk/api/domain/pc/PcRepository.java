package com.zqksk.api.domain.pc;

import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PcRepository {
    Pc save(final NewPc newPc);
    Pc updateScore(final Long id, final int score);
    List<Pc> findAll();
    List<PcResponse> findAllWithCondition(final boolean excludeTest, final int pgType,  final String searchType, final String searchText);
    Page<PcResponse> findAllWithConditionAndPaging(final int page, final int size, final boolean excludeTest, final int pgType, final String searchType, final String searchText);

    List<PcResponse> findAllWithMultipleCondition(final boolean excludeTest, final int pgType,Integer workType, String perfSpecName, final String searchType, final String searchText);
    Page<PcResponse> findAllWithMultipleConditionAndPaging(final int page, final int size, final boolean excludeTest, final int pgType,  Integer workType, String perfSpecName,final String searchType, final String searchText);

    Pc findById(final Long id);
    Pc findByMacAddress(final String macAddress);
    PcResponse findByHospitalIdAndMacAddressAndPgType(final String hospitalId, final String macAddress, final int pgType);
    Page<CurrentPcStatus> findCurrentPcStatusWithPage(int page, int size, boolean excludeTest, LocalDate startDate, LocalDate endDate, String searchType, String searchText);
    List<CurrentPcStatus> findCurrentPcStatus(boolean excludeTest, String searchType, String searchText);
    List<PmsUsageStatus> findPmsUsageStatus( boolean excludeTest, LocalDate startDate, LocalDate endDate, String searchType, String searchText);
    List<Pc> findAllByLastDateBetweenStartAndEnd(LocalDate startDate, LocalDate endDate);

    List<PerformanceSpec> findAllPerformanceSpecBySoftwareId(int softwareId);

    long deleteByWorkDatetimeAtBefore(LocalDateTime localDateTime);

    List<String> findAllHospitalIdList();

    List<CurrentPcStatus> findCurrentPcStatusByHospitalIds(boolean excludeTest, List<String> hospitalIds);

}
