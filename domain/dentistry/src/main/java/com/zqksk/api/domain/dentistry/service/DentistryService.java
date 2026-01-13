package com.zqksk.api.domain.dentistry.service;

import com.zqksk.api.domain.dentistry.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DentistryService {
    default Dentistry insertDentistry(DentistryRequest dentistryRequest) {
        throw new UnsupportedOperationException("Not implemented");
    }
    default DentistryWithClinicHoursResponse insertDentistryAndClinicHours(DentistryRequest dentistryRequest , DentistryClinicHoursRequest dentistryClinicHoursRequest) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default void changeClaimYnById(Long id, String claimYn){
        throw new UnsupportedOperationException("Not implemented");
    }

    default Page<DentistryResponse> getDentistryListWithPage(SearchDentistryRequestWithPage searchDentistryRequestWithPage) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default List<DentistryResponse> getDentistryList(SearchDentistryRequest searchDentistryRequest) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default List<DentistryUsagePcStatusResponse> getPmsUsageStatusWithCondition(SearchDentistryRequest searchDentistryRequest) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default List<Dentistry> getDentistryListWithBusinessRegistrationNumber(){
        throw new UnsupportedOperationException("Not implemented");
    }

    default void deleteDentistryClinicHours(){
        throw new UnsupportedOperationException("Not implemented");
    }

    default Long deleteAllByHospitalIdList(List<String> hospitalIds){
        throw new UnsupportedOperationException("Not implemented");
    }
}
