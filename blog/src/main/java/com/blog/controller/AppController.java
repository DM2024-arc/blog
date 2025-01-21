package com.blog.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.blog.model.Article;
import com.blog.service.ArticleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller 
public class AppController {
	
	@Autowired
	private ArticleService articleService;
	
	@Value("${uploadDir}")
	private String uploadfloder;
	
	@GetMapping("/administration")
	public String administration() {
		
		return "index" ;
	}
	
	@GetMapping("/gestionArticle")
	public String gestionArtilce(Model model) {
		
		List<Article> ListArticle = articleService.getAllArticles();
		 model.addAttribute("ListArticle", ListArticle);
		 return "administration/gestionArticle";
	}
	
	/**@GetMapping("/image/display/{id}")
	@ResponseBody
	public void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Article> article) throws IOException{
		
		article = articleService.getArticleById(id);
		
		response.setContentType("image/jpeg,image/jpg, image/jng, image/gif");
		response.getOutputStream().write(article.get().getImage());
		response.getOutputStream().close();	
	} */
	
	@GetMapping("/image/display/{id}")
	@ResponseBody
	public void showImage(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
	    Optional<Article> article = articleService.getArticleById(id);
	    
	    if (article.isPresent()) {
	        response.setContentType("image/jpeg"); // Définir le type en fonction de votre image
	        response.getOutputStream().write(article.get().getImage());
	        response.getOutputStream().close();
	    }
	}
	
	@GetMapping("/creationArticle")
	public String creationArticle(Model model, Article article) {
		
		model.addAttribute("article", article);
		return "administration/creationArticle";
			
	}
	
	@PostMapping("/enregistrerArticle")
	public RedirectView enregistrer(Model model, @RequestParam("titre") String titre,
												 @RequestParam("contenu") String contenu,
												 @RequestParam("redacteur") String redacteur,
												 HttpServletRequest request,
												 final @RequestParam("image") MultipartFile file) throws IOException{
		
		
		String uploadDirectory = request.getServletContext().getRealPath(uploadfloder);
		String fileName = file.getOriginalFilename();
		String filePath = Paths.get(uploadDirectory, fileName).toString();
		
		if(fileName == null || fileName.contains("..")) {
			model.addAttribute("invalid"); 
		}
		
		try {
			
			File dir = new File(uploadDirectory);
					
				if(!dir.exists()) {
					dir.mkdirs(); 
				}
				
				
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(file.getBytes());
				stream.close();
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		byte[] imageData = file.getBytes();
		
		Article articles = new Article(); 
		articles.setTitre(titre);
		articles.setContenue(contenu);
		articles.setRedacteur(redacteur);
		articles.setImage(imageData);
		
		articleService.saveArticle(articles);
			
		return new RedirectView("gestionArticle", true);
		
	}
	
	// Suppression d'un article 
	
	@GetMapping("/supprimerArticle/{id}")			
	public ModelAndView supprimer(@PathVariable("id") Long id) {
			
		Optional<Article> article = articleService.getArticleById(id);
		
		if(article.isPresent()) {
			
			articleService.deleteArticle(id);
			
			return new ModelAndView("redirect:/gestionArticle");
				
		}else {
			return new ModelAndView("redirect:/gestionArticle");
		}
			
	}
	
	
	// Modification d'un article 
	
	@GetMapping("modificationArticle/{id}")
	public String modificationArticle(@PathVariable(value = "id") Long id, Model model) {
	    Optional<Article> article = articleService.getArticleById(id);
	    // On déemballe l'Optional avant de l'ajouter au modèle
	    article.ifPresent(a -> model.addAttribute("article", a));
	    return "administration/modificationArticle";
	}
	
	@PostMapping("/modification/{id}")
	public RedirectView modification(Model model, @RequestParam("titre") String titre,
												 @PathVariable("id") Long id,
												 @RequestParam("contenue") String contenu,
												 @RequestParam("redacteur") String redacteur,
												 HttpServletRequest request,
												 final @RequestParam("image") MultipartFile file) throws IOException{
		
		
		String uploadDirectory = request.getServletContext().getRealPath(uploadfloder);
		String fileName = file.getOriginalFilename();
		String filePath = Paths.get(uploadDirectory, fileName).toString();
		
		if(fileName == null || fileName.contains("..")) {
			model.addAttribute("invalid"); 
		}
		
		try {
			
			File dir = new File(uploadDirectory);
					
				if(!dir.exists()) {
					dir.mkdirs(); 
				}
							
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(file.getBytes());
				stream.close();
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		byte[] imageData = file.getBytes();
		
		Article articles = articleService.getArticleById(id).orElse(null);
		
		articles.setTitre(titre);
		articles.setContenue(contenu);
		articles.setRedacteur(redacteur);
		articles.setImage(imageData);
		
		articleService.updateArticle(articles);
			
		return new RedirectView("/gestionArticle", true);
		
	}

}
