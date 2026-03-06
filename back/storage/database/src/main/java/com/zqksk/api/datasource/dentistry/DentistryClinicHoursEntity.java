package com.zqksk.api.datasource.dentistry;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.dentistry.DentistryClinicHours;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "dentistry_clinic_hours")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DentistryClinicHoursEntity extends BaseEntity {
    @Size(max = 15)
    @NotNull
    @Comment("요양기관번호")
    @Column(name = "hospital_id", nullable = false, length = 15)
    private String hospitalId;

    @Size(max = 62)
    @NotNull
    @Comment("진료시간(월|화|수|목|금|토|일)")
    @Column(name = "treat_time_terms", nullable = false, length = 40)
    private String treatTimeTerms;

    @Size(max = 62)
    @NotNull
    @Comment("점심시간(월|화|수|목|금|토|일)")
    @Column(name = "lunch_time_terms", nullable = false)
    private String lunchTimeTerms;

    @Size(max = 62)
    @Comment("저녁시간(월|화|수|목|금|토|일)")
    @Column(name = "dinner_time_terms", length = 20)
    private String dinnerTimeTerms;

    @Builder
    public DentistryClinicHoursEntity(String hospitalId, String treatTimeTerms, String lunchTimeTerms, String dinnerTimeTerms) {
        this.hospitalId = hospitalId;
        this.treatTimeTerms = treatTimeTerms;
        this.lunchTimeTerms = lunchTimeTerms;
        this.dinnerTimeTerms = dinnerTimeTerms;
    }

    public DentistryClinicHoursEntity update(String hospitalId, String treatTimeTerms, String lunchTimeTerms, String dinnerTimeTerms) {
        this.hospitalId = hospitalId;
        this.treatTimeTerms = treatTimeTerms;
        this.lunchTimeTerms = lunchTimeTerms;
        this.dinnerTimeTerms = dinnerTimeTerms;
        return this;
    }

    public DentistryClinicHours toDentistryClinicHours() {
        return DentistryClinicHours.builder()
                .id(super.getId())
                .hospitalId(hospitalId)
                .treatTimeTerms(treatTimeTerms)
                .lunchTimeTerms(lunchTimeTerms)
                .dinnerTimeTerms(dinnerTimeTerms)
                .build();
    }
}
