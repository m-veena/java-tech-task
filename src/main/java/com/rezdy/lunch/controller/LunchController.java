package com.rezdy.lunch.controller;

import com.rezdy.lunch.constant.CommonConstants;
import com.rezdy.lunch.entity.Recipe;
import com.rezdy.lunch.exception.InvalidIngredientsListException;
import com.rezdy.lunch.exception.ResourceNotFoundException;
import com.rezdy.lunch.service.LunchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/lunch")
public class LunchController {
	
	@Autowired
    private LunchService lunchService;

	/**
	 * This method returns list of recipes for which all its ingredients are under
	 * bestbefore date. If input date is not in yyyy-MM-dd format, error response is returned to the client
	 * example  - http://localhost:8080/lunch/2020-12-11
	 *  
	 * @param date - path variables BestBefore date for ingredients
	 * @return
	 */
	@GetMapping("/{date}")
	public List<Recipe> getRecipesBestBefore(@PathVariable("date") String bestBefore) {
		return lunchService.loadRecipeBestBefore(
				LocalDate.parse(bestBefore, DateTimeFormatter.ofPattern(CommonConstants.INPUT_DATE_FORMAT)));
	}

	/**
	 * This method returns list of recipes for which all its ingredients are under useby
	 * date. If input date is not in yyyy-MM-dd format, error response is returned to the client
	 * example  - http://localhost:8080/lunch/useBy?date=2020-12-11
	 * @param date - UseBy date for ingredients
	 * @return
	 */
	@GetMapping("/useBy/{date}")
	public List<Recipe> getRecipesUesBy(@PathVariable("date") String useByDate) {
		return lunchService.loadRecipeUseBy(
				LocalDate.parse(useByDate, DateTimeFormatter.ofPattern(CommonConstants.INPUT_DATE_FORMAT)));
	}

	/**
	 * This method returns the recipes for which ingredients have passed best before date but are under use by date. 
	 * If input date is not in yyyy-MM-dd format, error response is returned to the client
	 * example  - http://localhost:8080/lunch/afterBestBefore?date=2020-12-11 
	 * @param afterBestBefore
	 * @return
	 */
	@GetMapping("/afterBestBefore/{date}")
	public List<Recipe> getRecipesBetweenBestBeforeUseBy(@PathVariable("date") String afterBestBefore) {
		return lunchService.loadRecipeBetweenBestBeforeUseBy(
				LocalDate.parse(afterBestBefore, DateTimeFormatter.ofPattern(CommonConstants.INPUT_DATE_FORMAT)));
	}

	/**
	 * This method returns recipe if it exists in the database. It recipe doesn't exist, it returns error message to the client
	 * example  - http://localhost:8080/lunch/recipe/Salad
	 * @param title 
	 * @return
	 */
	@GetMapping("/recipe/{title}")
	public List<Recipe> getRecipesByName(@PathVariable("title") String title) {
		List<Recipe> recipe = lunchService.loadRecipeByTitle(title);
		if (recipe.isEmpty()) {
			throw new ResourceNotFoundException(CommonConstants.RESOURCE_NOT_FOUND_ERR_MSG);
		}
		return recipe;
	}
	
	/**
	 * This method returns recipe which don't have list of ingredients submitted by the client.
	 * Ingredients needs to be submitted in the format of - Cheese,Bread. Otherwise, error response is sent to the client 
	 * @param ingreditents
	 * @return
	 */
	@GetMapping("/filterOnIngredients/{ingreditents}")
	public List<Recipe> getRecipeUseByIngreditents(@PathVariable("ingreditents") String ingreditents) {
		Pattern pattern = Pattern.compile("^\\w+[,]?\\w+$");
		Matcher matcher = pattern.matcher(ingreditents);
		if (!matcher.matches()) {
			throw new InvalidIngredientsListException(CommonConstants.INVALID_INGREDIENTS_ERR_MSG);
		}
		return lunchService.loadRecipeByIngreditents(ingreditents);
	}
}
