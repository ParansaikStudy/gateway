package com.zqksk.api.datasource.user.entity;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.user.model.response.User;
import com.zqksk.api.domain.user.model.response.UserLogin;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Column(length = 50)
    String employeeNo;

    @Column(length = 100)
    private String password;

    @Column
    private String name;

    @Column(length = 100)
    @Pattern(regexp = "(?i)[a-z0-9]+(?:\\.[a-z0-9]+)*@[a-z]+\\.[a-z.]+")
    private String email;

    @Column(length = 100)
    private String departmentName;

    @Column(length = 100)
    private String positionName;

    @Builder
    public UserEntity(String employeeNo, String password, String name, String email, String departmentName, String positionName) {
        this.employeeNo = employeeNo;
        this.password = password;
        this.name = name;
        this.email = email;
        this.departmentName = departmentName;
        this.positionName = positionName;
    }

    public UserEntity updatePassword(String password) {
        this.password = password;
        return this;
    }

    public User toUser() {
        return new User(this.getId(), employeeNo, name, email, departmentName, positionName);
    }

    public UserLogin toUserLogin() {
        return new UserLogin(employeeNo, password);
    }
}
