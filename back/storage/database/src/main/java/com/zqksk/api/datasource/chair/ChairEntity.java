package com.zqksk.api.datasource.chair;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.chair.Chair;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chairs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChairEntity extends BaseEntity {
    private Long chairId;
    private String manufacturer;
    private String modelName;

    @Builder
    public ChairEntity(Long chairId, String manufacturer, String modelName) {
        this.chairId = chairId;
        this.manufacturer = manufacturer;
        this.modelName = modelName;
    }

    public Chair toChair() {
        return new Chair(
                getId(),
                chairId,
                manufacturer,
                modelName
        );
    }
}
