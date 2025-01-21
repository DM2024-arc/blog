package com.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.model.Article;

public interface ArticleRepository extends JpaRepository <Article, Long>{

}
