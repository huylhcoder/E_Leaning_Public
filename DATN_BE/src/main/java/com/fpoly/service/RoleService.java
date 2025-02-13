package com.fpoly.service;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fpoly.entity.Role;
import com.fpoly.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	RoleRepository roleRepo;

	public Optional<Role> findByName(String name) {
		Optional<Role> role = roleRepo.findByName(name);
		return role;
	}
}
