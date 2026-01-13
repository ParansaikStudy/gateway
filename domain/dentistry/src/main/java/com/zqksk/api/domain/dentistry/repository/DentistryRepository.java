package com.zqksk.api.domain.dentistry.repository;

import com.zqksk.api.domain.dentistry.Dentistry;
import com.zqksk.api.domain.dentistry.DentistryUsageStatus;
import com.zqksk.api.domain.dentistry.NewDentistry;

import java.util.List;

public interface DentistryRepository {
    Dentistry save(NewDentistry newDentistry);
    int updateContractInfo(Dentistry dentistry);
    int updateSidoAndSigungu(Dentistry dentistry);

    Dentistry findByHospitalId(String hospitalId);
    List<Dentistry> findAllByCriteria(boolean excludeTest, String searchType, String searchText);

    List<Dentistry> findAllByhBusinessRegistrationNumber();

    Long changeClaimYnById(Long id, String claimYn);

    List<DentistryUsageStatus> findPmsUsageStatus(boolean excludeTest, java.time.LocalDate startDate, java.time.LocalDate endDate, String searchType, String searchText);

    List<String> findAllHospitalIdList();


    Long deleteAllByHospitalIdList(List<String> hospitalIds);
}
