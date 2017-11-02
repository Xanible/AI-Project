// CSC 416
// Semester Project

//Competition class to handle each of the 3 competition rounds

package main;

import java.util.*;

public class Competition
{
    /*
     * Initialize 4 chefs as a list
     * 
     * Initialize pantry Array of ingredient objects Array of quantities of
     * ingredients
     * 
     * Start Each competition round Randomized priority access to the pantry for
     * each chef
     * 
     * Array of Recipes stored here Public method to getRecipe
     * 
     * Handle judging with a score based on: How many basket ingredients are used
     * How much of each ingredient is used? Does the dish fit the challenge theme?
     * How complex is the dish How difficult was the dish to make? Does the dish
     * make sense?
     * 
     * Eliminate a chef The chef with the lowest score each successive round is
     * eliminated
     */
    private ArrayList<Cook> cooks;
    private Ingredient[] ingredients = { new Ingredient("Apple", "Fruit"), new Ingredient("Bread", "Grain"),
            new Ingredient("Spinach", "Vegetable"), new Ingredient("Egg", "Meat"),
            new Ingredient("Potato", "Vegetable"), new Ingredient("Celery", "Vegetable"),
            new Ingredient("Beef", "Meat"), new Ingredient("Jalapeno", "Pepper"), new Ingredient("Pineapple", "Fruit"),
            new Ingredient("Tomato", "Fruit"), new Ingredient("Pork", "Meat") };
    /*
     * "Apple", "Bread", "Spinach", "Egg", "Potato", "Celery", "Beef", "Jalapeno",
     * "Pineapple", "Tomato", "Pork", "Bacon", "Chicken", "Crackers", "Quail eggs",
     * "Biscuits", "Caviar", "Duck eggs", "Whole raw chicken", "Waffles",
     * "Bleu cheese", "Siracha", "Enoki mushrooms", "Pear", "Kiwi", "Blueberry",
     * "Poblano pepper", "Cauliflower", "Smoked salmon", "Squid", "Fried rice",
     * "Marshmallows", "Corn flakes", "Banana pudding", "Chocolate chips",
     * "Rack of lamb", "Maple syrup", "Sea urchin", "Popcorn"
     */
    private Ingredient[] pantry;
    private int[] pantryQuantities;
    private Recipe[] recipes = {};
    private String theme;

    public Competition()
    {
        // Create list of chefs
        cooks = new ArrayList<Cook>();
        cooks.add(new Cook());
        cooks.add(new Cook());
        cooks.add(new Cook());
        cooks.add(new Cook());

        pantry = new Ingredient[4];
        pantryQuantities = new int[4];
    }

    public String beginRound(String[] pantry, String theme)
    {
        String output = "";
        this.theme = theme;
        
        // Initialize pantry
        for(int i = 0; i < pantry.length; i++)
        {
            this.pantry[i] = getIngredientByName(pantry[i]);
            pantryQuantities[i] = (int)(Math.random() * 400 + 1);
        }
        
        // Allow chefs to access pantry
        shuffleCooks();
        for(int i = 0; i < cooks.size(); i++)
        {
            //cooks.get(i).accessPantry(pantry, pantryQuantities);
        }
        
        // Judge the dishes
        for(int i = 0; i < cooks.size(); i++)
        {
            // Recipe dish = cooks.get(i).getDish();
            //cooks.get(i).setScore(evaluateDish(dish));
        }
        
        // Eliminate the losing chef
        cooks.remove(cooks.size() - 1);
        
        return output;
    }
    
    public Ingredient getIngredientByName(String name)
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
    
    public ArrayList<Ingredient> getIngredientsByCategory(String category)
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
    
    public Recipe[] getRecipes()
    {
        return recipes;
    }
    
    public String getTheme()
    {
        return theme;
    }
    
    /*private void sortCooks()
    {
        for(int i = 0; i < cooks.size(); i++)
        {
            int max = i;
            
            for(int j = i + 1; j < cooks.size(); j++)
            {
                if(cooks.get(j).getScore() > cooks.get(max).getScore())
                {
                    max = j;
                }
            }
            
            if(max != i)
            {
                Cook temp = cooks.get(i);
                cooks.set(i, cooks.get(max));
                cooks.set(max, temp);
            }
        }
    }*/
    
    private void shuffleCooks()
    {
        for(int i = 0; i < cooks.size(); i++)
        {
            int ind;
            do
            {
                ind = (int)(Math.random() * cooks.size());
            }
            while(ind == i);
            
            Cook temp = cooks.get(ind);
            cooks.set(ind, cooks.get(i));
            cooks.set(i, temp);
        }
    }
    
    private int evaluateDish()
    {
        return -1;
    }
}
