package com.zqksk.api.datasource.dentistry;

import com.zqksk.api.domain.dentistry.*;
import com.zqksk.api.domain.dentistry.component.DentistryClinicHoursRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.zqksk.api.datasource.dentistry.QDentistryClinicHoursEntity.dentistryClinicHoursEntity;

@Repository
@RequiredArgsConstructor
public class DentistryClinicHoursCoreRepository implements DentistryClinicHoursRepository {

    private final DentistryClinicHoursJpaRepository dentistryClinicHoursJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public DentistryClinicHours save(NewDentistryClinicHours newDentistryClinicHours) {

        DentistryClinicHoursEntity dentistryClinicHoursEntity = dentistryClinicHoursJpaRepository.findByHospitalId(newDentistryClinicHours.hospitalId())
                .orElse(null);

        if(dentistryClinicHoursEntity != null) {
            return dentistryClinicHoursJpaRepository.save(
                    dentistryClinicHoursEntity.update(
                            dentistryClinicHoursEntity.getHospitalId(),
                            newDentistryClinicHours. treatTimeTerms(),
                            newDentistryClinicHours.lunchTimeTerms(),
                            newDentistryClinicHours.dinnerTimeTerms()
                    )
            ).toDentistryClinicHours();
        }

        return dentistryClinicHoursJpaRepository.save(DentistryClinicHoursEntity.builder()
                .hospitalId(newDentistryClinicHours.hospitalId())
                .treatTimeTerms(newDentistryClinicHours.treatTimeTerms())
                .lunchTimeTerms(newDentistryClinicHours.lunchTimeTerms())
                .dinnerTimeTerms(newDentistryClinicHours.dinnerTimeTerms())
                .build()
        ).toDentistryClinicHours();
    }

    @Override
    public DentistryClinicHours findByHospitalId(String hospitalId) {
        return dentistryClinicHoursJpaRepository.findByHospitalId(hospitalId)
                .map(DentistryClinicHoursEntity::toDentistryClinicHours)
                .orElse(null);
    }

    @Override
    public List<String> findAllHospitalIdList() {
        return jpaQueryFactory.select(dentistryClinicHoursEntity.hospitalId)
                .from(dentistryClinicHoursEntity)
                .fetch();
    }

    @Override
    public Long deleteAllByHospitalIdList(List<String> hospitalIds) {
        return jpaQueryFactory.delete(dentistryClinicHoursEntity)
                .where(dentistryClinicHoursEntity.hospitalId.in(hospitalIds))
                .execute();
    }
}
