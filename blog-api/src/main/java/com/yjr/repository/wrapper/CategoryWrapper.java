package com.yjr.repository.wrapper;

import com.yjr.vo.CategoryVO;

import java.util.List;


public interface CategoryWrapper {

    List<CategoryVO> findAllDetail();

    CategoryVO getCategoryDetail(Integer categoryId);
}
