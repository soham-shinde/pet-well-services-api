
package com.petwellservices.api.service.category;

import java.util.List;

import com.petwellservices.api.entities.Category;
import com.petwellservices.api.entities.City;

public interface ICategoryService {
    Category createCategory(Category category);
    List<Category> getAllCategories();
    Category getCategoryById(Long categoryId);
    void deleteCategoryById(Long categoryId);
    Category updateCategory(Long categoryId, String categoryName);
}
