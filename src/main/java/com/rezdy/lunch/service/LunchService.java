package com.rezdy.lunch.service;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rezdy.lunch.entity.Recipe;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LunchService {
	
	private static final Logger logger = LoggerFactory.getLogger(LunchService.class);
    @Autowired
    private EntityManager entityManager;

    private List<Recipe> recipesSorted;

    public List<Recipe> getNonExpiredRecipesOnDate(LocalDate date) {
        List<Recipe> recipes = loadRecipes(date);

        sortRecipes(recipes);

        return recipesSorted;
    }

    private void sortRecipes(List<Recipe> recipes) {
        recipesSorted = recipes; //TODO sort recipes considering best-before
    }

    public List<Recipe> loadRecipes(LocalDate date) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> criteriaQuery = cb.createQuery(Recipe.class);
        Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);

        CriteriaQuery<Recipe> query = criteriaQuery.select(recipeRoot);

        Subquery<Recipe> nonExpiredIngredientSubquery = query.subquery(Recipe.class);
        Root<Recipe> nonExpiredIngredient = nonExpiredIngredientSubquery.from(Recipe.class);
        nonExpiredIngredientSubquery.select(nonExpiredIngredient);

        Predicate matchingRecipe = cb.equal(nonExpiredIngredient.get("title"), recipeRoot.get("title"));
        Predicate expiredIngredient = cb.lessThan(nonExpiredIngredient.join("ingredients").get("useBy"), date);

        Predicate allNonExpiredIngredients = cb.exists(nonExpiredIngredientSubquery.where(matchingRecipe, expiredIngredient));

        return entityManager.createQuery(query.where(allNonExpiredIngredients)).getResultList();
    }
    
	/**
	 * This method will return recipes whose all ingredients are under best before date passed as parameter
	 * 	 - Get all recipes
	 *   - Get recipes which have ingredients who bestBefore date is less than submitted by client
	 *   - Minus these recipes from all recipes
	 * @param date 
	 * @param date
	 * @return
	 */
	public List<Recipe> loadRecipeBestBefore(LocalDate date) {
		logger.info("loadRecipeBestBefore: - {}", date);
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recipe> criteriaQuery = cb.createQuery(Recipe.class);
		CriteriaQuery<Recipe> criteriaQueryForAllRecipes = cb.createQuery(Recipe.class);
		Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);

		Root<Recipe> recipeRootForAllRecipe = criteriaQueryForAllRecipes.from(Recipe.class);
		TypedQuery<Recipe> typedQuery = entityManager
				.createQuery(criteriaQueryForAllRecipes.select(recipeRootForAllRecipe));

		CriteriaQuery<Recipe> query = criteriaQuery.select(recipeRoot);

		Subquery<String> expiredIngredientSubquery = criteriaQuery.subquery(String.class);
		Root<Recipe> expiredIngredient = expiredIngredientSubquery.from(Recipe.class);
		expiredIngredientSubquery.select(expiredIngredient.get("title"));

		Predicate matchingRecipe = cb.equal(expiredIngredient.get("title"), recipeRoot.get("title"));

		Predicate expiredIngredients = cb.lessThan(expiredIngredient.join("ingredients").get("bestBefore"), date);

		Predicate allExpiredIngredients = cb
				.exists(expiredIngredientSubquery.where(expiredIngredients, matchingRecipe));

		List<Recipe> expiredRecipes = entityManager.createQuery(query.where(allExpiredIngredients)).getResultList();
		List<Recipe> allRecipes = typedQuery.getResultList();

		logger.info("loadRecipeBestBefore: End");

		return (List<Recipe>) CollectionUtils.subtract(allRecipes, expiredRecipes);
	}

	/**
	 * This method returns list of recipes for which all its ingredients are under useby date. 
	 * - Get all recipes
	 *   - Get recipes which have ingredients who useBy date is less than submitted by client
	 *   - Minus these recipes from all recipes
	 * @param date
	 * @return
	 */
	public List<Recipe> loadRecipeUseBy(LocalDate date) {
		logger.info("loadRecipeUseBy: - {}", date);

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recipe> criteriaQuery = cb.createQuery(Recipe.class);
		CriteriaQuery<Recipe> criteriaQueryForAllRecipes = cb.createQuery(Recipe.class);
		Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);
		Root<Recipe> recipeRootForAllRecipe = criteriaQueryForAllRecipes.from(Recipe.class);
		TypedQuery<Recipe> typedQuery = entityManager
				.createQuery(criteriaQueryForAllRecipes.select(recipeRootForAllRecipe));

		CriteriaQuery<Recipe> query = criteriaQuery.select(recipeRoot);

		Subquery<String> expiredIngredientSubquery = criteriaQuery.subquery(String.class);
		Root<Recipe> expiredIngredient = expiredIngredientSubquery.from(Recipe.class);
		expiredIngredientSubquery.select(expiredIngredient.get("title"));

		Predicate matchingRecipe = cb.equal(expiredIngredient.get("title"), recipeRoot.get("title"));

		Predicate expiredIngredients = cb.lessThan(expiredIngredient.join("ingredients").get("useBy"), date);

		Predicate allExpiredIngredients = cb
				.exists(expiredIngredientSubquery.where(expiredIngredients, matchingRecipe));

		List<Recipe> expiredRecipes = entityManager.createQuery(query.where(allExpiredIngredients)).getResultList();
		expiredRecipes.stream().forEach(System.out::println);
		List<Recipe> allRecipes = typedQuery.getResultList();
		allRecipes.stream().forEach(System.out::println);
		logger.info("loadRecipeUseBy: End");
		return (List<Recipe>) CollectionUtils.subtract(allRecipes, expiredRecipes);
	}
	
	/**
	 * This method returns the recipes for which ingredients have passed best before date but are under use by date. 
	 * @param date
	 * @return
	 */
	public List<Recipe> loadRecipeBetweenBestBeforeUseBy(LocalDate date) {
		logger.info("loadRecipeBetweenBestBeforeUseBy: - {}", date);

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recipe> criteriaQuery = cb.createQuery(Recipe.class);
		CriteriaQuery<Recipe> criteriaQueryForAllRecipes = cb.createQuery(Recipe.class);
		Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);

		CriteriaQuery<Recipe> query = criteriaQuery.select(recipeRoot);

		Subquery<String> expiredIngredientSubquery = criteriaQuery.subquery(String.class);
		Root<Recipe> expiredIngredient = expiredIngredientSubquery.from(Recipe.class);
		expiredIngredientSubquery.select(expiredIngredient.get("title"));

		Predicate matchingRecipe = cb.equal(expiredIngredient.get("title"), recipeRoot.get("title"));

		Join join1 = expiredIngredient.join("ingredients");
		Join join2 = expiredIngredient.join("ingredients");

		Predicate afterBestBeforeIngredients = cb.greaterThan(join1.get("bestBefore"), date);

		Predicate beforeUseByIngredients = cb.lessThan(join2.get("useBy"), date);

		Predicate bestBeforeAndUseBy = cb.exists(expiredIngredientSubquery
				.where(cb.or(afterBestBeforeIngredients, beforeUseByIngredients), matchingRecipe));

		logger.info("loadRecipeBetweenBestBeforeUseBy: - End");
		return entityManager.createQuery(query.where(bestBeforeAndUseBy)).getResultList();
	}

	/**
	 *  This method returns recipe if it exists in the database. otherwise empty list
	 * @param title
	 * @return
	 */
	public List<Recipe> loadRecipeByTitle(String title) {
		logger.info("loadRecipeByTitle: - {} ", title);
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recipe> criteriaQuery = cb.createQuery(Recipe.class);
		Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);

		criteriaQuery.select(recipeRoot.get("title"))
				.where(cb.equal(cb.lower(recipeRoot.get("title")), title.toLowerCase()));
		logger.info("loadRecipeByTitle: - End");
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	/**
	 * This method returns recipe which don't have list of ingredients submitted by the client.
	 * Implementation Details
	 *   - Get all recipes
	 *   - Get recipes which has ingredients from the client
	 *   - Minus these recipes from all recipes
	 *   - It will give the list of recipes which don't have submitted ingredients
	 * @param ingredients
	 * @return
	 */
	public List<Recipe> loadRecipeByIngreditents(String ingredients) {

		logger.info("loadRecipeBetweenBestBeforeUseBy: - {} ", ingredients);

		List<String> ingredientsSubmitted = Arrays.asList(ingredients.split(","));

		System.out.println("ingredientsArr: " + ingredientsSubmitted);
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Recipe> criteriaQuery = cb.createQuery(Recipe.class);
		CriteriaQuery<Recipe> criteriaQueryForAllRecipes = cb.createQuery(Recipe.class);
		Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);
		Root<Recipe> recipeRootForAllRecipe = criteriaQueryForAllRecipes.from(Recipe.class);
		TypedQuery<Recipe> typedQuery = entityManager
				.createQuery(criteriaQueryForAllRecipes.select(recipeRootForAllRecipe));

		CriteriaQuery<Recipe> query = criteriaQuery.select(recipeRoot);

		Join join1 = recipeRoot.join("ingredients");

		List<Predicate> predicates = new ArrayList<Predicate>();
		for (String value : ingredientsSubmitted) {
			predicates.add(cb.equal(cb.lower(join1.get("title")), value.toLowerCase()));
		}

		List<Recipe> expiredRecipes = entityManager
				.createQuery(query.where(cb.or(predicates.toArray(new Predicate[] {})))).getResultList();
		List<Recipe> allRecipes = typedQuery.getResultList();
		logger.info("loadRecipeBetweenBestBeforeUseBy: - End");
		return (List<Recipe>) CollectionUtils.subtract(allRecipes, expiredRecipes);

	}

}
