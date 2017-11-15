// CSC 416
// Semester Project

//Cook class to handle each of the 4 chefs and their dishes

package main;

import java.util.*;

public class Cook
{
    /*
     * Hard code 4 chefs and their cooking tendencies/preferences such as: Name as
     * string Skill level as integer 1-5 Array of preferred ingredients Array of
     * avoided ingredients (i.e. allergies) Preferred level of complexity as integer
     * Array of themes/recipes they’re phenomenal at Recipes being used as objects
     * 
     */
    private String name;
    private int skill;
    private int preferredComplexity;
    private String[] preferredIngredients;
    private String[] avoidedIngredients;
    private String[] excelAt;
    private int score;
    private Recipe[] availableRecipes;
    private Recipe[] dishes;
    //private Ingredient[] supplies;
    //private int[] quantities;

    public Cook(String name, int skill, int preferredComplexity, String[] preferred, String[] avoided, String[] excelAt)
    {
        this.name = name;
        this.skill = skill;
        this.preferredComplexity = preferredComplexity;
        this.preferredIngredients = preferred;
        this.avoidedIngredients = avoided;
        this.excelAt = excelAt;
        availableRecipes = Recipe.getRecipesByDifficulty(skill);
    }

    public String getName()
    {
        return name;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int s)
    {
        score = s;
    }

    public int getSkill()
    {
        return skill;
    }

    public int getPreferredComplexity()
    {
        return preferredComplexity;
    }

    public String[] getPreferredIngredients()
    {
        return preferredIngredients;
    }

    public String[] getAvoidedIngredients()
    {
        return avoidedIngredients;
    }

    public String[] getExeclAt()
    {
        return excelAt;
    }

    // Stub - should apply heuristics on recipe list
    public Recipe[] getDishes()
    {
        return dishes;
    }

    // Stub - should apply heuristics and claim the best of the remaining pantry
    // ingredients -- need to add list for available ingredients/quantities
    public int[] accessPantry(Ingredient[] emphasized, int[] quantities)
    {
        return quantities;
    }
    
    private ArrayList<Recipe> rankPossibleRecipes(Ingredient[] emphasized, int[] quantities)
    {
        ArrayList<Recipe> possible = new ArrayList<Recipe>();
        
        for(int i = 0; i < 5; i++)
        {
            if(preferredComplexity <= skill && i == 0)
            {
                evaluateRecipes(preferredComplexity, possible, emphasized, quantities);
            }
        }
        
        sortListByGoodness(possible);
        return possible;
    }
    
    private void evaluateRecipes(int x, ArrayList<Recipe> possible, Ingredient[] emphasized, int[] quantities)
    {
        
    }
    
    private boolean matchIngredients(Recipe r, int[] quantities)
    {
        String[] ings = r.getIngredients();
        Quantity[] qs = r.getQuantities();
        
        for(int i = 0; i < ings.length; i++)
        {
            if(ings[i].charAt(0) == '-')
            {
                // If optional the available quantity doesn't matter
            }
            else if(ings[i].charAt(0) == '*')
            {
                // If exact then look up the quantity of the specified ingredient
                int ind = Ingredient.getIndexOfIngredient(ings[i].substring(1));
                if(quantities[ind] < qs[i].getNum())
                {
                    return false;
                }
            }
            else
            {
                // If c
            }
        }
        
        return true;
    }
    
    private void sortListByGoodness(ArrayList<Recipe> recipes)
    {
        for(int i = 0; i < recipes.size(); i++)
        {
            int max = i;
            
            for(int j = 1; j < recipes.size(); j++)
            {
                if(recipes.get(j).getGoodness() > recipes.get(max).getGoodness())
                {
                    max = j;
                }
            }
            
            if(max != i)
            {
                Recipe temp = recipes.get(max);
                recipes.set(max, recipes.get(i));
                recipes.set(i, temp);
            }
        }
    }
}