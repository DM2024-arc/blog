package com.blog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.model.Article;
import com.blog.repository.ArticleRepository;

@Service 
@Transactional
public class ArticleService {
	@Autowired
	private ArticleRepository repo;
	
	// Récuperation des articles 
	
	public List<Article>getAllArticles(){
		  
		return repo.findAll();	
	}
	
	// Récuperation d'un seul article 
	
	public Optional<Article>getArticleById(Long id ){
		
		return repo.findById(id);
		
	}
	
	// Sauvergarder un article 
	
	public void saveArticle(Article article) {
		
		repo.save(article);
	}
	
	// Suppression d'un article 
	
	public void deleteArticle(Long id) {
		repo.deleteById(id);
	}
	
	// Modification  d'un article
	
	public void updateArticle(Article updateArticle ) {
		
		Optional<Article> existingArticleOptional = repo.findById(updateArticle.getId());
		 
			if(existingArticleOptional.isPresent()) {
				
				Article existingArticles = existingArticleOptional.get();
				
				existingArticles.setTitre(updateArticle.getTitre());
				existingArticles.setContenue(updateArticle.getContenue());
				existingArticles.setRedacteur(updateArticle.getRedacteur());
				existingArticles.setImage(updateArticle.getImage());
				 repo.saveAndFlush(existingArticles);
			}
		
	}
	
						
}
