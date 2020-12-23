package com.rezdy.lunch.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rezdy.lunch.controller.LunchController;
import com.rezdy.lunch.entity.Recipe;
import com.rezdy.lunch.exception.ResourceNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LunchServiceTest {
	
	@Autowired
	 LunchController lunchController;
	
	
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
