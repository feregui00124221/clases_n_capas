package com.renegz.pnccontroller.services;

import com.renegz.pnccontroller.domain.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAllCategories();
    Category findCategoryByCode(String code);
}
