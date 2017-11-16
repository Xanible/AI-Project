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
    private Recipe[] dishes = new Recipe[0];
    // private Ingredient[] supplies;
    // private int[] quantities;

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

    public Recipe[] getDishes()
    {
        return dishes;
    }

    public int[] accessPantry(int[] quantities)
    {
        ArrayList<Recipe> ranked = rankPossibleRecipes(quantities);
        ArrayList<Recipe> cooked = new ArrayList<Recipe>();
        int timeUsed = 0;
        this.score = 0;

        boolean cookedDish;
        do
        {
            cookedDish = false;

            // Select the highest ranked recipe that requires less than or equal to the time
            // remaining
            for(int i = 0; i < ranked.size(); i++)
            {
                if(ranked.get(i).getTime() <= (Competition.getTimeLimit() - timeUsed))
                {
                    cooked.add(ranked.get(i));
                    timeUsed = timeUsed + ranked.get(i).getTime();

                    String[] ingredients = ranked.get(i).getIngredients();
                    for(int j = 0; j < ingredients.length; j++)
                    {
                        quantities[Ingredient.getIndexOfIngredient(
                                ingredients[j])] = quantities[Ingredient.getIndexOfIngredient(ingredients[j])]
                                        - ranked.get(i).getQuantities()[j].getNum();
                    }
                    cookedDish = true;
                    break;
                }
            }

            if(cookedDish == false)
            {
                break;
            }
            else
            {
                // Available ingredient quantities have changed - recompute the rankings
                ranked = rankPossibleRecipes(quantities);
            }
        }
        while(true);

        this.dishes = cooked.toArray(new Recipe[cooked.size()]);
        return quantities;
    }

    private ArrayList<Recipe> rankPossibleRecipes(int[] quantities)
    {
        ArrayList<Recipe> possible = new ArrayList<Recipe>();

        for(int i = 0; i < 5; i++)
        {
            if(i == 0)
            {
                possible = evaluateRecipes(preferredComplexity, possible, quantities, 0);
            }
            else
            {
                // Bounds checking
                if(preferredComplexity + i > 5 && preferredComplexity - i < 1)
                {
                    break;
                }
                else
                {
                    if(preferredComplexity + i <= 5)
                    {
                        possible = evaluateRecipes(preferredComplexity + i, possible, quantities, i);
                    }
                    if(preferredComplexity - i > 0)
                    {
                        possible = evaluateRecipes(preferredComplexity - i, possible, quantities, i);
                    }
                }
            }
        }

        possible = sortListByGoodness(possible);
        return possible;
    }

    // Scoring weights:
    // Theme matches = 100
    // Basket ingredient = 25 per
    // Preferred ingredient = 10 per
    // Avoided ingredient = -200 per
    // Should be rebalanced to include weights once the recipe list is created
    private ArrayList<Recipe> evaluateRecipes(int x, ArrayList<Recipe> possible, int[] quantities, int dist)
    {
        ArrayList<Recipe> complex = filterByComplexity(x);

        for(Recipe r : complex)
        {
            Recipe m = matchIngredients(r, quantities);
            if(m != null)
            {
                // Set the score to the distance parameter (20 - 5 * dist)
                int score = 20 - 5 * dist;

                // Determine which of its categories the recipe matches
                String[] matchedCategories = checkCategories(m);
                if(inList(Competition.getTheme(), matchedCategories))
                {
                    score = score + 100;
                }

                // Sum the goodness scores of the ingredients
                String[] newIngs = new String[m.getIngredients().length];
                String[] ings = m.getIngredients();
                for(int i = 0; i < ings.length; i++)
                {
                    score = score + Integer.parseInt(ings[i].substring(0, ings[i].indexOf('*')));
                    newIngs[i] = ings[i].substring(ings[i].indexOf('*') + 1);
                }
                
                Recipe eRec = new Recipe(m.getName(), m.getDifficulty(), m.getTime(), m.getComplexity(), m.getCategories(), newIngs, m.getQuantities());
                eRec.setGoodness(score);
                possible.add(eRec);
            }
        }
        
        return possible;
    }

    private Recipe matchIngredients(Recipe r, int[] quantities)
    {
        String[] ings = r.getIngredients();
        Quantity[] qs = r.getQuantities();
        ArrayList<String> newIngs = new ArrayList<String>();
        ArrayList<Quantity> newQuantities = new ArrayList<Quantity>();

        for(int i = 0; i < ings.length; i++)
        {
            // If ingredient is option
            if(ings[i].charAt(0) == '-')
            {
                String eing = evaluateByCategory(ings[i], i, quantities, qs);

                if(eing == null)
                {
                    // This ingredient is optional, it doesn't matter that no valid substitution was
                    // found
                }
                else
                {
                    int goodness = Integer.parseInt(eing.substring(0, eing.indexOf('*')));

                    // Ingredient is either preferred or in the baseket. Use it
                    if(goodness > 0)
                    {
                        newIngs.add(eing);
                        newQuantities.add(qs[i]);
                    }
                }
            }
            // If ingredient is specified exactly
            else if(ings[i].charAt(0) == '*')
            {
                // If exact then look up the quantity of the specified ingredient
                int ind = Ingredient.getIndexOfIngredient(ings[i].substring(1));

                // If there is not enough of the ingredient return null
                if(quantities[ind] < qs[i].getNum())
                {
                    return null;
                }
                else
                {
                    // Otherwise evaluate the ingredient
                    String eing = evaluateIngredient(ings[i].substring(1));
                    newIngs.add(eing);
                    newQuantities.add(qs[i]);
                }
            }
            // If ingredient is a category
            else
            {
                String eing = evaluateByCategory(ings[i], i, quantities, qs);

                if(eing == null)
                {
                    return null;
                }
                else
                {
                    newIngs.add(eing);
                    newQuantities.add(qs[i]);
                }
            }
        }

        return new Recipe(r.getName(), r.getDifficulty(), r.getTime(), r.getComplexity(), r.getCategories(),
                newIngs.toArray(new String[newIngs.size()]), newQuantities.toArray(new Quantity[newQuantities.size()]));
    }

    private String evaluateByCategory(String ing, int i, int[] quantities, Quantity[] requiredQuantities)
    {
        // If category then pull the list of ingredients and evaluate each
        ArrayList<Ingredient> cat = Ingredient.getIngredientsByCategory(ing);
        String retVal = null;
        int best = -8000; // Set to a massive negative number to ensure that an ingredient is selected if
                          // only avoided ingredients are present

        for(int j = 0; j < cat.size(); j++)
        {
            int ind = Ingredient.getIndexOfIngredient(cat.get(j).getName());
            if(quantities[ind] >= requiredQuantities[i].getNum())
            {
                // Evaluate the goodness of each ingredient
                String eing = evaluateIngredient(cat.get(j).getName());
                int curr = Integer.parseInt(eing.substring(0, eing.indexOf('*')));

                // If ingredient is the best seen so far save it in the concrete ingredient list
                if(curr > best)
                {
                    retVal = eing;
                    best = curr;
                }
            }
        }

        // If no valid ingredients found in the category the return will be null
        return retVal;
    }

    private String evaluateIngredient(String ing)
    {
        int curr = 0;
        if(inList(ing, preferredIngredients))
        {
            curr = curr + 10;
        }
        if(inList(ing, avoidedIngredients))
        {
            curr = curr - 200;
        }
        if(inList(ing, Competition.getBasket()))
        {
            curr = curr + 25;
        }

        return curr + "*" + ing;
    }

    private ArrayList<Recipe> sortListByGoodness(ArrayList<Recipe> recipes)
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
        
        return recipes;
    }

    private ArrayList<Recipe> filterByComplexity(int c)
    {
        ArrayList<Recipe> filtered = new ArrayList<Recipe>();

        for(int i = 0; i < availableRecipes.length; i++)
        {
            if(availableRecipes[i].getComplexity() == c)
            {
                filtered.add(availableRecipes[i]);
            }
        }

        return filtered;
    }

    private String[] checkCategories(Recipe r)
    {
        ArrayList<String> categories = new ArrayList<String>();

        for(String c : r.getCategories())
        {
            if(c.equals("Vegetarian"))
            {
                boolean isVegetarian = true;
                for(int i = 0; i < r.getIngredients().length; i++)
                {
                    if(inList(Ingredient.getIngredientByName(r.getIngredients()[i]).getCategory(), Ingredient.meats))
                    {
                        isVegetarian = false;
                        break;
                    }
                }

                if(isVegetarian)
                {
                    categories.add(c);
                }
            }
            else
            {
                categories.add(c);
            }
        }

        return categories.toArray(new String[categories.size()]);
    }

    private boolean inList(String theme, String[] categories)
    {
        for(int i = 0; i < categories.length; i++)
        {
            if(categories[i].equals(theme))
            {
                return true;
            }
        }

        return false;
    }
}