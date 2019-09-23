package com.yjr.repository;

import com.yjr.entity.Article;
import com.yjr.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;




public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByArticleAndLevelOrderByCreateDateDesc(Article a, String level);


}
