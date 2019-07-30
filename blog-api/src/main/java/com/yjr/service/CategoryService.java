package com.yjr.service;

import com.yjr.entity.Category;
import com.yjr.vo.CategoryVO;

import java.util.List;

/**
 * @author shimh
 * <p>
 * 2018年1月25日
 */
public interface CategoryService {

    List<Category> findAll();

    Category getCategoryById(Integer id);

    Integer saveCategory(Category category);

    Integer updateCategory(Category category);

    void deleteCategoryById(Integer id);

    List<CategoryVO> findAllDetail();

    CategoryVO getCategoryDetail(Integer categoryId);

}
