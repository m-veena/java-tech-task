package com.rezdy.lunch.entity;

import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity class represents Ingredient table structure in DB. And have reference to recipe Set for many to many relationp. It means
 * one recipe can refer to multiple ingredients and one ingredient can refer to multiple recipes
 * @author madhu
 *
 */

@Entity
@Table(name = "ingredient")
public class Ingredient {
	@Id
	@Column(name = "title")
	private String title;

	@Column(name = "best_before")
	private LocalDate bestBefore;

	@Column(name = "use_by")
	private LocalDate useBy;

	@ManyToMany(mappedBy = "ingredients", fetch = FetchType.LAZY)
	private Set<Recipe> recipies = new HashSet<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getBestBefore() {
		return bestBefore;
	}

	public void setBestBefore(LocalDate bestBefore) {
		this.bestBefore = bestBefore;
	}

	public LocalDate getUseBy() {
		return useBy;
	}

	public void setUseBy(LocalDate useBy) {
		this.useBy = useBy;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Ingredient))
			return false;
		Ingredient ingredient = (Ingredient) other;
		return ingredient.title.equals(title);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
