package com.blog.model;

import java.lang.reflect.GenericArrayType;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity 
@Table(name = "article")
public class Article {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable =false,unique =true)
	private long id ;
	
	private String titre ;
	private String contenue ;
	private String redacteur;
	
	@Lob
	@Column (name = "Image", length = Integer.MAX_VALUE, nullable = true)
	private byte[] Image ;

	// Construteur sans parametres 
	public Article() {
		super();
	}
		
			// 	Setter et Getter 
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getContenue() {
		return contenue;
	}

	public void setContenue(String contenue) {
		this.contenue = contenue;
	}

	public String getRedacteur() {
		return redacteur;
	}

	public void setRedacteur(String redacteur) {
		this.redacteur = redacteur;
	}

	public byte[] getImage() {
		return Image;
	}

	public void setImage(byte[] image) {
		Image = image;
	}
	


}
