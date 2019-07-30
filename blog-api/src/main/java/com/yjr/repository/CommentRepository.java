package com.yjr.repository;

import java.util.List;

import com.yjr.entity.Article;
import com.yjr.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;



/**
 * @author shimh
 * <p>
 * 2018年1月25日
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByArticleAndLevelOrderByCreateDateDesc(Article a, String level);


}
