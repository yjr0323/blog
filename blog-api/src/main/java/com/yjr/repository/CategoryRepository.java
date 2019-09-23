package com.yjr.repository;

import com.yjr.entity.Category;
import com.yjr.repository.wrapper.CategoryWrapper;
import org.springframework.data.jpa.repository.JpaRepository;




public interface CategoryRepository extends JpaRepository<Category, Integer>, CategoryWrapper {

	/*@Query(value="select c.*, count(a.category_id) as articles from me_category c "
			+ "left join me_article a on a.category_id = c.id group by a.category_id",nativeQuery=true)
	List<Category> findAllDetail();*/

}
