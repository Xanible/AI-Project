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
    
	private String name;
	private String category;
	private int flavor;
	private int spicyness;
	private int sweetness;
	
	public Ingredient(String name, String category, int flavor, int spicy, int sweet)
	{
	    this.name = name;
	    this.category = category;
	    this.flavor = flavor;
	    spicyness = spicy;
	    sweetness = sweet;
	}
	
	public String getName()
	{
	    return name;
	}
	
	public String getCategory()
	{
	    return category;
	}
	
	public int getFlavor()
	{
	    return flavor;
	}
	
	public int getSpicyness()
    {
        return spicyness;
    }
	
	public int getSweetness()
    {
        return sweetness;
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
            if(ingredients[i].getName().equals(category))
            {
                inCategory.add(ingredients[i]);
            }
        }

        return inCategory;
    }
}
