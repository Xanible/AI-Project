// CSC 416
// Semester Project

//Ingredient class to store ingredient information

package main;

import java.util.ArrayList;

public class Ingredient {
	
/*
	Ingredient objects have the following information:
		Name as string
		Category as string
*/
    public static Ingredient[] ingredients;
    public static String[] meats = new String[] {"pork", "poultry", "beef", "meat_other", "seafood"};
	private String name;
	private String category;
	private int spicy;
	private int bitter;
	private int pungent;
	private int sweet;
	private int umami;
	
	public Ingredient(String name, String category, int spicy, int bitter, int pungent, int sweet, int umami)
	{
	    this.name = name;
	    this.category = category;
	    this.spicy = spicy;
	    this.bitter = bitter;
	    this.pungent = pungent;
	    this.sweet = sweet;
	    this.umami = umami;
	}
	
	public String getName()
	{
	    return name;
	}
	
	public String getCategory()
	{
	    return category;
	}
	
	public int getSpicy()
	{
	    return spicy;
	}
	
	public int getBitter()
    {
        return bitter;
    }
	
	public int getPungent()
    {
        return pungent;
    }
	
	public int getSweet()
	{
	    return sweet;
	}
	
	public int getUmami()
	{
	    return umami;
	}
	
	public static Ingredient getIngredientByName(String name)
    {
        for(int i = 0; i < ingredients.length; i++)
        {
            if(ingredients[i].getName().equals(name))
            {
                return ingredients[i];
            }
        }

        return null;
    }
	
	public static ArrayList<Ingredient> getIngredientsByCategory(String category)
    {
        ArrayList<Ingredient> inCategory = new ArrayList<Ingredient>();

        for(int i = 0; i < ingredients.length; i++)
        {
            if(ingredients[i].getCategory().equals(category))
            {
                inCategory.add(ingredients[i]);
            }
        }

        return inCategory;
    }
	
	public static int getIndexOfIngredient(String ing)
    {
        for(int i = 0; i < Ingredient.ingredients.length; i++)
        {
            if(ing.equals(Ingredient.ingredients[i].getName()))
            {
                return i;
            }
        }
        
        return -1;
    }
}