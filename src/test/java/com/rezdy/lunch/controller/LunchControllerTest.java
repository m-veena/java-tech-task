package com.rezdy.lunch.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.rezdy.lunch.entity.Recipe;
import com.rezdy.lunch.exception.ResourceNotFoundException;

@SpringBootTest( webEnvironment = WebEnvironment.RANDOM_PORT)
public class LunchControllerTest {
	
	@Autowired
	private LunchController lunchController;
	
	@Test
	public void contextLoads() {
		assertThat(lunchController).isNotNull();
	}
	
	@Test
	public void testLoadRecipeByTitle()
	{	
		List<Recipe> rows = lunchController.getRecipesByName("Salad");
		Object obj = rows.get(0);				
		assertEquals("Salad",obj.toString());		
	}

	@Test
	public void testLoadRecipeByTitleException() 
	{		
		assertThrows(ResourceNotFoundException.class, ()-> {
			lunchController.getRecipesByName("XX");
		});
	}
	
}
