package com.zqksk.api.domain.user.component;

import com.zqksk.api.domain.user.model.response.Role;
import com.zqksk.api.domain.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@RequiredArgsConstructor
public class RoleFinder {
    private final RoleRepository roleRepository;

    public List<Role> getRoles(){
        return roleRepository.findAll();
    }

    public Role getRoleByType(String type){
        return roleRepository.findByType(type);
    }

    public Role getRoleById(Long userId){
        return roleRepository.findById(userId);
    }
}
