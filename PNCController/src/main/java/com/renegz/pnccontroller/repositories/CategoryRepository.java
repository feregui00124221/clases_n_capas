package com.renegz.pnccontroller.repositories;

import com.renegz.pnccontroller.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository
        extends JpaRepository<Category, String> { // El esquema de datos y el tipo de ID

}
