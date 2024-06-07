package com.renegz.pnccontroller.repositories;

import com.renegz.pnccontroller.domain.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {

}
