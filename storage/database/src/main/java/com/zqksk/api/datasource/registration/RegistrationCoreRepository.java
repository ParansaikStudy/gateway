package com.zqksk.api.datasource.registration;

import com.zqksk.api.domain.registration.model.Registration;
import com.zqksk.api.domain.registration.service.RegistrationRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RegistrationCoreRepository implements RegistrationRepository {
    private final RegistrationJpaRepository registrationJpaRepository;

    public RegistrationCoreRepository(RegistrationJpaRepository registrationJpaRepository) {
        this.registrationJpaRepository = registrationJpaRepository;
    }

    @Override
    public void save(Registration registration) {
        RegistrationEntity registrationEntity = new RegistrationEntity(
                registration.uid(),
                registration.registrationDate(),
                registration.registrationId(),
                registration.registrationSequence(),
                registration.patientDivision() == null || registration.patientDivision().isEmpty() ? null : PatientDivision.from(registration.patientDivision().toUpperCase()),
                registration.patientStatus(),
                registration.dentalId(),
                registration.doctorId(),
                registration.hygienistId(),
                registration.chairId(),
                registration.reservationDate(),
                registration.registrationStatus()
        );

        registrationJpaRepository.save(registrationEntity);
    }

    @Override
    public List<Registration> findByRegistrationDateBetween(LocalDateTime from, LocalDateTime to) {
        List<RegistrationEntity> registrations = registrationJpaRepository.findByRegistrationDateBetween(from, to);
        return registrations.stream()
                .map(RegistrationEntity::toRegistration)
                .toList();
    }
}
