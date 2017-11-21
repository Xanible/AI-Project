// CSC 416
// Semester Project

//Recipe class to store recipe information
package main;

import java.util.*;

public class Recipe
{
    /*
     * Stores array of recipes, where each recipe has a: Name as string Difficulty
     * as an integer from 1-5 Array of categories Array of ingredients
     */
    public static Recipe[] recipes = {};

    private String name;
    private int difficulty;
    private int time;
    private int complexity;
    private String[] categories;
    private String[] ingredients;
    private String[] subComponents;
    private Quantity[] quantities;
    private int spicy;
    private int bitter;
    private int pungent;
    private int sweet;
    private int umami;
    
    // Heuristic value used for selection
    private int goodness;

    public Recipe(String name, int difficulty, int time, int complexity, String[] categories, String[] ingredients, String[] subComponents, Quantity[] quantities, int spicy, int bitter, int pungent, int sweet, int umami)
    {
        this.name = name;
        this.difficulty = difficulty;
        this.time = time;
        this.complexity = complexity;
        this.categories = categories;
        this.ingredients = ingredients;
        this.subComponents = subComponents;
        this.quantities = quantities;
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

    public String[] getSubComponents()
    {
        return subComponents;
    }
    public Quantity[] getQuantities()
    {
        return quantities;
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
    
    public int getGoodness()
    {
        return goodness;
    }
    
    public void setGoodness(int g)
    {
        goodness = g;
    }

    public String toString()
    {
        String rec = (name.charAt(0) + "").toUpperCase() + name.substring(1) + "\nMade with:\n";

        for(int i = 0; i < ingredients.length; i++)
        {
            rec = rec + quantities[i] + " of ";

            if(ingredients[i].charAt(0) == '-' || ingredients[i].charAt(0) == '*')
            {
                rec = rec + ingredients[i].substring(1);
            }
            else
            {
                rec = rec + ingredients[i];
            }

            if(i < quantities.length - 1)
            {
                rec = rec + "\n";
            }
        }

        return rec;
    }

    public static Recipe[] getRecipes()
    {
        return recipes;
    }
    
    public static Recipe[] getRecipesByDifficulty(int difficulty)
    {
        ArrayList<Recipe> filtered = new ArrayList<Recipe>();
        
        for(int i = 0; i < recipes.length; i++)
        {
            if(recipes[i].getDifficulty() <= difficulty)
            {
                filtered.add(recipes[i]);
            }
        }
        
        return filtered.toArray(new Recipe[filtered.size()]);
    }
}