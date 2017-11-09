// CSC 416
// Semester Project

//Recipe class to store recipe information
package main;

public class Recipe {
	/*
	Stores array of recipes, where each recipe has a:
		Name as string
		Difficulty as an integer from 1-5
		Array of categories
		Array of ingredients
	*/
    public static Recipe[] recipes = {};
    
    private String name;
    private int difficulty;
    private String[] categories;
    private Ingredient[] ingredients;
    
    public Recipe(String name, int difficulty, String[] categories, Ingredient[] ingredients)
    {
        this.name = name;
        this.difficulty = difficulty;
        this.categories = categories;
        this.ingredients = ingredients;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getDifficulty()
    {
        return difficulty;
    }
    
    public String[] getCategories()
    {
        return categories;
    }
    
    public Ingredient[] getIngredients()
    {
        return ingredients;
    }
    
    public Recipe[] getRecipes()
    {
        return recipes;
    }
}
