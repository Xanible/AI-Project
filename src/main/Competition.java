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
    private static String[] emphasizedIngredients;
    private static String theme;
    private static int timeLimit;

    private ArrayList<Cook> cooks;
    private int[] pantryQuantities;

    public Competition()
    {
        // Create list of chefs
        cooks = pickRandomCooks(4);

        timeLimit = 30;
        emphasizedIngredients = new String[4];
        pantryQuantities = new int[Ingredient.ingredients.length];
    }

    public String beginRound(String[] basket, String newTheme)
    {
        String output = "";
        theme = newTheme;

        if(Main.isDemo)
        {
            try
            {
                pantryQuantities = convertToIntArray(Main.readDemoQuantities());
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return "Error";
            }
        }
        else
        {

            for(int i = 0; i < pantryQuantities.length; i++)
            {
                // Multiply by 100 to allow for fractional ingredient requirements
                pantryQuantities[i] = (int) ((Math.random() * 21) * 100);
            }

        }

        for(int i = 0; i < basket.length; i++)
        {
            emphasizedIngredients[i] = basket[i];
            int index = Ingredient.getIndexOfIngredient(basket[i]);
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

            if(cooks.get(i).getDishes().length == 0)
            {
                // Cooks should be penalized for not cooking anything
                cooks.get(i).setScore(-10000);
            }
            for(int j = 0; j < dish.length; j++)
            {
                cooks.get(i).setScore(cooks.get(i).getScore() + evaluateDish(cooks.get(i), dish[j]));
            }

            output = output + cooks.get(i).getName() + ", scoring " + cooks.get(i).getScore() + " with:\n";
            for(int j = 0; j < dish.length; j++)
            {
                output = output + dish[j];

                if(j < dish.length - 1)
                {
                    output = output + "\n\n";
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

    public static int[] convertToIntArray(Integer[] arr)
    {
        int[] ret = new int[arr.length];

        for(int i = 0; i < ret.length; i++)
        {
            ret[i] = arr[i];
        }

        return ret;
    }

    private ArrayList<Cook> pickRandomCooks(int count)
    {
        ArrayList<Cook> random = new ArrayList<Cook>();
        ArrayList<Integer> inds = new ArrayList<Integer>();

        for(int i = 0; i < count; i++)
        {
            int ind = (int) (Math.random() * Cook.cooks.length);
            while(inds.contains(ind))
            {
                ind = (int) (Math.random() * Cook.cooks.length);
            }

            random.add(Cook.cooks[ind]);
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

    private int evaluateDish(Cook cook, Recipe dish)
    {
        int score = 0;

        // Check that dish matches the theme of the round
        String[] categories = Cook.checkCategories(dish);
        if(!(Cook.inList(theme, categories)))
        {
            score = score - 200;
        }
        // Check if the cook excels at this recipe
        if(Cook.inList(dish.getName(), cook.getExeclAt()))
        {
            score = score + 45;
        }

        // Ingredient level scoring
        String[] ings = dish.getIngredients();
        for(int i = 0; i < ings.length; i++)
        {
            if(Cook.inList(ings[i], emphasizedIngredients))
            {
                score = score + 30;
            }
            int dist = Cook.computeFlavorDistance(dish, Ingredient.getIngredientByName(ings[i]));
            score = score - 20 * dist;
        }

        return score;
    }
}
