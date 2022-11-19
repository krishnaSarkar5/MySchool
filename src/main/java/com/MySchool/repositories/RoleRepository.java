package com.MySchool.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MySchool.entities.master.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByIdAndStatus(Long roleId,Integer status);
}
