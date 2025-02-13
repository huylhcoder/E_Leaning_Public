package com.fpoly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fpoly.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	public Optional<Role> findByName(String name);
}
