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
     * Handle judging with a score based on: How many basket ingredients are used
     * How much of each ingredient is used? Does the dish fit the challenge theme?
     * How complex is the dish How difficult was the dish to make? Does the dish
     * make sense?
     * 
     * Eliminate a chef The chef with the lowest score each successive round is
     * eliminated
     */
    public static String[] themes;
    public static Cook[] cookList;

    private ArrayList<Cook> cooks;
    private static String[] emphasizedIngredients;
    private int[] pantryQuantities;
    private static String theme;
    private static int timeLimit;

    public Competition()
    {
        // Create list of chefs
        cooks = pickRandomCooks(4);

        timeLimit = 30;
        emphasizedIngredients = new String[4];
        pantryQuantities = new int[Ingredient.ingredients.length];
    }

    public String beginRound(String[] pantry, String newTheme)
    {
        String output = "";
        theme = newTheme;

        // Initialize pantry
        for(int i = 0; i < pantryQuantities.length; i++)
        {
            pantryQuantities[i] = (int) (Math.random() * 401);
        }
        
        // Testing, set all breads to 2
        /*ArrayList<Ingredient> ings = Ingredient.getIngredientsByCategory("bread");
        for(int i = 0; i < ings.size(); i++)
        {
            int ind = Ingredient.getIndexOfIngredient(ings.get(i).getName());
            pantryQuantities[ind] = 2;
        }*/
        
        for(int i = 0; i < pantry.length; i++)
        {
            emphasizedIngredients[i] = pantry[i];
            int index = Ingredient.getIndexOfIngredient(pantry[i]);
            pantryQuantities[index] = 100000000;
        }

        // Allow chefs to access pantry
        shuffleCooks();
        for(int i = 0; i < cooks.size(); i++)
        {
            pantryQuantities = cooks.get(i).accessPantry(pantryQuantities);
        }

        // Judge the dishes
        for(int i = 0; i < cooks.size(); i++)
        {
            Recipe[] dish = cooks.get(i).getDishes();
            
            for(int j = 0; j < dish.length; j++)
            {
                cooks.get(i).setScore(cooks.get(i).getScore() + evaluateDish(dish[j]));
            }
            
            
            output = output + cooks.get(i).getName() + ", scoring " + cooks.get(i).getScore() + " with:\n";
            for(int j = 0; j < dish.length; j++)
            {
                output = output + dish[j];
                
                if(j < dish.length - 1)
                {
                    output = output + "\n";
                }
            }
            if(i < cooks.size() - 1)
            {
                output = output + "\n\n";
            }
            else
            {
                output = output + "\n";
            }
        }
        sortCooks();

        // Eliminate the losing chef
        output = output + "\n" + cooks.get(cooks.size() - 1).getName() + " is eliminated!";
        cooks.remove(cooks.size() - 1);
        
        if(cooks.size() == 1)
        {
            output = output + "\n" + cooks.get(0).getName() + " wins!";
        }

        return output;
    }

    public static String getTheme()
    {
        return theme;
    }
    
    public static String[] getBasket()
    {
        return emphasizedIngredients;
    }
    
    public static int getTimeLimit()
    {
        return timeLimit;
    }

    private ArrayList<Cook> pickRandomCooks(int count)
    {
        ArrayList<Cook> random = new ArrayList<Cook>();
        ArrayList<Integer> inds = new ArrayList<Integer>();

        for(int i = 0; i < count; i++)
        {
            int ind = (int) (Math.random() * cookList.length);
            while(inds.contains(ind))
            {
                ind = (int) (Math.random() * cookList.length);
            }

            random.add(cookList[ind]);
            inds.add(ind);
        }

        return random;
    }

    private void sortCooks()
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
    }

    private void shuffleCooks()
    {
        for(int i = 0; i < cooks.size(); i++)
        {
            int ind;
            do
            {
                ind = (int) (Math.random() * cooks.size());
            }
            while(ind == i);

            Cook temp = cooks.get(ind);
            cooks.set(ind, cooks.get(i));
            cooks.set(i, temp);
        }
    }

    private int evaluateDish(Recipe dish)
    {
        return -1;
    }
}
