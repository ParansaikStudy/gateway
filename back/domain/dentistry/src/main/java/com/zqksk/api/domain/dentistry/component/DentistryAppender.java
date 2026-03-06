package com.zqksk.api.domain.dentistry.component;

import com.zqksk.api.domain.dentistry.Dentistry;
import com.zqksk.api.domain.dentistry.NewDentistry;
import com.zqksk.api.domain.dentistry.repository.DentistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DentistryAppender {

    private final DentistryRepository dentistryRepository;

    public Dentistry appendDentistry(NewDentistry newDentistry) {
        return dentistryRepository.save(newDentistry);
    }

    public void changeClaimYnById(Long id, String claimYn){ dentistryRepository.changeClaimYnById(id,claimYn);}

    public int updateContractInfo(Dentistry dentistry) {
        return dentistryRepository.updateContractInfo(dentistry);
    }

    public int updateDentistrySidoAndSigungu(Dentistry dentistry) {
        return dentistryRepository.updateSidoAndSigungu(dentistry);
    }

    public Long deleteAllByHospitalIdList(List<String> hospitalIds){
        return dentistryRepository.deleteAllByHospitalIdList(hospitalIds);
    }
}
