package com.movie_theaters.repository;

import com.movie_theaters.entity.Movie;
import com.movie_theaters.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRoleName(String role);
}
