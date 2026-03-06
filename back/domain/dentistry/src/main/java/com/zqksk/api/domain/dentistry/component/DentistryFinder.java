package com.zqksk.api.domain.dentistry.component;

import com.zqksk.api.domain.dentistry.Dentistry;
import com.zqksk.api.domain.dentistry.DentistryUsageStatus;
import com.zqksk.api.domain.dentistry.SearchType;
import com.zqksk.api.domain.dentistry.repository.DentistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DentistryFinder {

    private final DentistryRepository dentistryRepository;

    public Dentistry getDentistry(String hospitalId) {
        return dentistryRepository.findByHospitalId(hospitalId);
    }

    public List<Dentistry> getDentistryList(boolean excludeTest, String searchType, String searchText) {
        String type = searchType;

        try{
            SearchType.fromValue(searchType);
        }catch (IllegalArgumentException e){
            type = null;
        }
        return dentistryRepository.findAllByCriteria(excludeTest, type, searchText);
    }

    public List<Dentistry> getDentistryListWithBusinessRegistrationNumber(){
        return dentistryRepository.findAllByhBusinessRegistrationNumber();
    }

    public List<DentistryUsageStatus> findPmsUsageStatus(boolean excludeTest, LocalDate startDate, LocalDate endDate, String searchType, String searchText) {
        return dentistryRepository.findPmsUsageStatus(excludeTest, startDate, endDate, searchType, searchText);
    }

    public List<String> getAllHospitalIdList(){
        return dentistryRepository.findAllHospitalIdList();
    }
}
