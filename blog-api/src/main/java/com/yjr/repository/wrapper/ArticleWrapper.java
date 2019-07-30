package com.yjr.repository.wrapper;

import com.yjr.entity.Article;
import com.yjr.vo.ArticleVo;
import com.yjr.vo.PageVo;

import java.util.List;

public interface ArticleWrapper {
    List<Article> listArticles(PageVo page);

    List<Article> listArticles(ArticleVo article, PageVo page);

    List<ArticleVo> listArchives();

}
