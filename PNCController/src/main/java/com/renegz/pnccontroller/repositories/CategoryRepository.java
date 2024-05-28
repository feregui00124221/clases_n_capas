package com.renegz.pnccontroller.repositories;

import com.renegz.pnccontroller.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findCategoryByName(String name);
}
