package com.renegz.pnccontroller.services.implementations;

import com.renegz.pnccontroller.domain.entities.Category;
import com.renegz.pnccontroller.repositories.CategoryRepository;
import com.renegz.pnccontroller.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryByCode(String category) {
        return categoryRepository.findCategoryByName(category).orElse(null);
    }
}
