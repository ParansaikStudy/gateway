package com.zqksk.api.domain.pc;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PcFinder {

    private final PcRepository pcRepository;

    public List<Pc> getPcList() {
        return pcRepository.findAll();
    }

    public List<PcResponse> getPcListWithCondition(boolean excludeTest, int pgType, String searchType, String searchText) {
        return pcRepository.findAllWithCondition(excludeTest, pgType,  searchType, searchText);
    }


    public Page<PcResponse> getPcListWithConditionAndPaging(int page, int size, boolean excludeTest, int pgType, String searchType, String searchText) {
        return pcRepository.findAllWithConditionAndPaging(page, size, excludeTest, pgType, searchType, searchText);
    }

    public List<PcResponse> getPcListWithMultipleCondition(boolean excludeTest, int pgType,  Integer workType, String perfSpecName, String searchType, String searchText) {
        return pcRepository.findAllWithMultipleCondition(excludeTest, pgType, workType, perfSpecName, searchType, searchText);
    }


    public Page<PcResponse> getPcListWithMultipleConditionAndPaging(int page, int size, boolean excludeTest, int pgType,  Integer workType, String perfSpecName, String searchType, String searchText) {
        return pcRepository.findAllWithMultipleConditionAndPaging(page, size, excludeTest, pgType,workType, perfSpecName, searchType, searchText);
    }

    public Pc getPc(Long id) {
        return pcRepository.findById(id);
    }

    public Pc getPcByMacAddress(String macAddress) {
        return pcRepository.findByMacAddress(macAddress);
    }

    public PcResponse getPcByHospitalIdAndMacAddressAndPgType(String hospitalId, String macAddress, int pgType) {
        return pcRepository.findByHospitalIdAndMacAddressAndPgType(hospitalId, macAddress, pgType);
    }

    public Page<CurrentPcStatus> getCurrentPcStatusListWithPage(int page, int size, boolean excludeTest, LocalDate startDate, LocalDate endDate, String searchType, String searchText) {
        return pcRepository.findCurrentPcStatusWithPage(page, size, excludeTest, startDate, endDate, searchType, searchText);
    }

    public List<CurrentPcStatus> getCurrentPcStatusList(boolean excludeTest, String searchType, String searchText) {
        String type = searchType;

        try{
            SearchType.fromValue(searchType);
        }catch (IllegalArgumentException e){
            type = null;
        }


        return pcRepository.findCurrentPcStatus(excludeTest, type, searchText);
    }

    public List<PmsUsageStatus> findPmsUsageStatus(boolean excludeTest, LocalDate startDate, LocalDate endDate, String searchType, String searchText) {
        return pcRepository.findPmsUsageStatus( excludeTest, startDate, endDate, searchType, searchText);
    }

    public List<CurrentPcStatus> getCurrentPcStatusList() {
        return Collections.emptyList();
    }

    public List<Pc> getListByLastDateBetweenStartAndEnd(LocalDate startDate, LocalDate endDate){
        return pcRepository.findAllByLastDateBetweenStartAndEnd(startDate, endDate);
    }

    public List<PerformanceSpec> getAllPerformanceSpecBySoftwareId(int softwareId) {
        return pcRepository.findAllPerformanceSpecBySoftwareId(softwareId);
    }

    public List<String> getAllHospitalIdList(){
        return pcRepository.findAllHospitalIdList();
    }

    public List<CurrentPcStatus> getCurrentPcStatusByHospitalIds(boolean excludeTest, List<String> hospitalIds) {
        return pcRepository.findCurrentPcStatusByHospitalIds(excludeTest, hospitalIds);
    }
}
