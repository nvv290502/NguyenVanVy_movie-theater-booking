package com.movie_theaters.service.impl;

import com.movie_theaters.entity.Role;
import com.movie_theaters.repository.RoleRepository;
import com.movie_theaters.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
