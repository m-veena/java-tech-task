package com.rezdy.lunch.entity;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * This entity class represents recipe table structure in DB. And have reference to ingredients Set for many to many relationp. It means
 * one recipe can refer to multiple ingredients and one ingredient can refer to multiple recipes
 * @author madhu
 *
 */

@Entity
@Table(name = "recipe")
public class Recipe {

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "recipe_ingredient", joinColumns = @JoinColumn(name = "recipe"), inverseJoinColumns = @JoinColumn(name = "ingredient"))
	private Set<Ingredient> ingredients = new HashSet<>();

	@Id
	@Column(name = "title")
	private String title;
	
	public Recipe() {
		super();
	}
	public Recipe(String title) {
		this.title = title;		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (!(other instanceof Recipe)) return false;
		Recipe recipe = (Recipe) other;
		return recipe.title.equals(title);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
