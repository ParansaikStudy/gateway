package com.zqksk.api.datasource.user.entity;

import com.zqksk.api.datasource.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestUserEntity extends BaseEntity {


    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 50)
    private String name;

    @NotNull
    @Column(length = 50)
    private String employeeNo;

    @Builder
    public TestUserEntity(String email, String password, String name, String employeeNo) {
        this.password = password;
        this.name = name;
        this.employeeNo = employeeNo;
    }


}
