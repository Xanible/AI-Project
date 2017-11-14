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
    private int time;
    private int complexity;
    private String[] categories;
    private String[] ingredients;
    private String[] optionalIngredients;
    private Quantity[] quantities;
    
    public Recipe(String name, int difficulty, int time, int complexity, String[] categories, String[] ingredients, String[] optIngredients, Quantity[] quantities)
    {
        this.name = name;
        this.difficulty = difficulty;
        this.time = time;
        this.complexity = complexity;
        this.categories = categories;
        this.ingredients = ingredients;
        this.optionalIngredients = optIngredients;
        this.quantities = quantities;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getDifficulty()
    {
        return difficulty;
    }
    
    public int getTime()
    {
        return time;
    }
    
    public int getComplexity()
    {
        return complexity;
    }
    
    public String[] getCategories()
    {
        return categories;
    }
    
    public String[] getIngredients()
    {
        return ingredients;
    }
    
    public String[] getOptionalIngredients()
    {
        return optionalIngredients;
    }
    
    public Recipe[] getRecipes()
    {
        return recipes;
    }
    
    public Quantity[] getQuantites()
    {
        return quantities;
    }
}
