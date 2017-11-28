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
    public static Cook[] cooks;

    private String name;
    private int skill;
    private int preferredComplexity;
    private String[] preferredIngredients;
    private String[] avoidedIngredients;
    private String[] excelAt;
    private int score;
    private Recipe[] availableRecipes;
    private Recipe[] dishes = new Recipe[0];

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
                    // If goodness is above a given threshold and recipe has not been cooked before
                    if(ranked.get(i).getGoodness() >= 50 && !compareRecipeLists(ranked.get(i), cooked))
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
                    else
                    {
                        continue;
                    }
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

    private static boolean compareRecipeLists(Recipe ranked, ArrayList<Recipe> cooked)
    {
        for(int i = 0; i < cooked.size(); i++)
        {
            if(cooked.get(i).getName().equals(ranked.getName()))
            {
                return true;
            }
        }
        
        return false;
    }

    public static int computeFlavorDistance(Recipe r, Ingredient i)
    {
        int dist = 0;

        // If the recipe expects 0 but the ingredient has 1 then add to dist
        if(r.getUmami() == 0 && i.getUmami() == 1)
        {
            dist = dist + 1;
        }
        if(r.getSpicy() == 0 && i.getSpicy() == 1)
        {
            dist = dist + 1;
        }
        if(r.getSweet() == 0 && i.getSweet() == 1)
        {
            dist = dist + 1;
        }
        if(r.getBitter() == 0 && i.getBitter() == 1)
        {
            dist = dist + 1;
        }
        if(r.getPungent() == 0 && i.getPungent() == 1)
        {
            dist = dist + 1;
        }

        // If the ingredient is pungent and not in the basket then add 10 to flavor
        // distance
        if(i.getPungent() == 1 && !(inList(i.getName(), Competition.getBasket())))
        {
            dist = dist + 10;
        }

        return dist;
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
    // Theme matches = 200
    // Distance from preferred complexity = 20 - 5 * dist
    // Flavor distance = -20 * flavorDist
    // Basket ingredient = 25 per
    // Preferred ingredient = 10 per
    // Avoided ingredient = -200 per
    // Could be rebalanced to include weights once the recipe list is created
    private ArrayList<Recipe> evaluateRecipes(int x, ArrayList<Recipe> possible, int[] quantities, int dist)
    {
        ArrayList<Recipe> complex = filterByComplexity(x);
        for(Recipe r : complex)
        {
            Recipe m = matchIngredients(r, quantities);
            if(m != null)
            {
                System.out.println("Recipe " + m.getName());
            }

            if(m != null)
            {
                // Set the score to the distance parameter (20 - 5 * dist)
                int score = 20 - 5 * dist;
                System.out.println("Distance score " + score);

                // Determine which of its categories the recipe matches
                String[] matchedCategories = checkCategories(m);
                if(inList(Competition.getTheme(), matchedCategories) || inList(m.getName(), Recipe.subcomponentNames))
                {
                    score = score + 200;
                    System.out.println("Category score " + 200);
                }

                // Sum the goodness scores of the ingredients
                String[] newIngs = new String[m.getIngredients().length];
                String[] ings = m.getIngredients();
                for(int i = 0; i < ings.length; i++)
                {
                    score = score + Integer.parseInt(ings[i].substring(0, ings[i].indexOf('*')));
                    newIngs[i] = ings[i].substring(ings[i].indexOf('*') + 1);
                    System.out.println("Ingredient " + newIngs[i] + " score is "
                            + Integer.parseInt(ings[i].substring(0, ings[i].indexOf('*'))));
                }

                Recipe eRec = new Recipe(m.getName(), m.getDifficulty(), m.getTime(), m.getComplexity(),
                        m.getCategories(), newIngs, m.getSubComponents(), m.getQuantities(), m.getSpicy(),
                        m.getBitter(), m.getPungent(), m.getSweet(), m.getUmami(), false);

                eRec.setGoodness(score);
                possible.add(eRec);

                System.out.println("Final score is " + score);
            }
        }

        return possible;
    }

    private Recipe matchIngredients(Recipe r, int[] quantities)
    {
        String[] ings = r.getIngredients();
        Quantity[] qs = r.getQuantities();
        String[] optIngs = r.getOptionalIngredients();
        Quantity[] optQs = r.getOptionalQuantities();
        int[] used = new int[quantities.length];
        int timeNeeded = 0;
        ArrayList<String> newIngs = new ArrayList<String>();
        ArrayList<Quantity> newQuantities = new ArrayList<Quantity>();

        // Evaluate the required ingredients
        for(int i = 0; i < ings.length; i++)
        {
            // If ingredient is specified exactly
            if(ings[i].charAt(0) == '*')
            {
                // If exact then look up the quantity of the specified ingredient
                int ind = Ingredient.getIndexOfIngredient(ings[i].substring(1));

                // If there is not enough of the ingredient return null
                if(quantities[ind] < qs[i].getNum() + used[ind])
                {
                    return null;
                }
                else
                {
                    // Otherwise evaluate the ingredient
                    used[ind] = used[ind] + qs[i].getNum();
                    String eing = evaluateIngredient(r, ings[i].substring(1));
                    newIngs.add(eing);
                    newQuantities.add(qs[i]);
                }
            }
            // If ingredient is a category
            else
            {
                // Pull the list of ingredients and evaluate each
                ArrayList<Ingredient> cat = Ingredient.getIngredientsByCategory(ings[i]);
                String eing = null;
                String bestIng = null;
                int bestInd = -1;
                int best = -8000; // Set to a massive negative number to ensure that an ingredient is selected if
                                  // only avoided ingredients are present

                for(int j = 0; j < cat.size(); j++)
                {
                    int ind = Ingredient.getIndexOfIngredient(cat.get(j).getName());
                    if(quantities[ind] >= qs[i].getNum() + used[ind])
                    {
                        // Evaluate the goodness of each ingredient
                        eing = evaluateIngredient(r, cat.get(j).getName());
                        int curr = Integer.parseInt(eing.substring(0, eing.indexOf('*')));

                        // If ingredient is the best seen so far save it in the concrete ingredient list
                        if(curr > best)
                        {
                            bestInd = ind;
                            bestIng = eing;
                            best = curr;
                        }
                    }
                }

                if(bestIng == null)
                {
                    return null;
                }
                else
                {
                    used[bestInd] = used[bestInd] + qs[i].getNum();
                    newIngs.add(bestIng);
                    newQuantities.add(qs[i]);
                }
            }
        }

        // Evaluate the required subcomponents
        for(int i = 0; i < r.getSubComponents().length; i++)
        {
            if(r.getSubComponents()[i].charAt(0) != '-')
            {
                ArrayList<Recipe> subRecs = getRecipesByCategory(availableRecipes, r.getSubComponents()[i]);
                Recipe best = null;
                int bestScore = -8000;

                for(int j = 0; j < subRecs.size(); j++)
                {
                    Recipe subR = matchIngredients(subRecs.get(j), computeDifferenceArray(quantities, used));
                    int score = 0;

                    if(subR == null)
                    {
                        return null;
                    }
                    else
                    {
                        String[] eings = subR.getIngredients();
                        for(int k = 0; k < eings.length; k++)
                        {
                            score = score + Integer.parseInt(eings[i].substring(0, eings[i].indexOf('*')));

                            if(score > bestScore)
                            {
                                bestScore = score;
                                best = subR;
                            }
                        }
                    }
                }

                // If subcomponent cannot be made
                if(best == null)
                {
                    return null;
                }
                else
                {
                    // Otherwise add the ingredients to the list of new ingredients, update the used
                    // list, and add the time required for the subcomponent
                    for(int l = 0; l < best.getIngredients().length; l++)
                    {
                        String eing = best.getIngredients()[l];
                        newIngs.add(eing);
                        newQuantities.add(best.getQuantities()[l]);
                        int ind = Ingredient.getIndexOfIngredient(eing.substring(eing.indexOf('*') + 1));
                        used[ind] = used[ind] + best.getQuantities()[l].getNum();
                    }

                    timeNeeded = timeNeeded + best.getTime();
                }
            }
        }

        // Evaluate the optional ingredients
        for(int i = 0; i < optIngs.length; i++)
        {
            // If ingredient is specified exactly
            if(optIngs[i].charAt(0) == '*')
            {
                // If exact then look up the quantity of the specified ingredient
                int ind = Ingredient.getIndexOfIngredient(optIngs[i].substring(1));

                // If there is not enough of the ingredient skip it
                if(quantities[ind] < optQs[i].getNum() + used[ind])
                {
                    continue;
                }
                else
                {
                    // Otherwise evaluate the ingredient
                    String eing = evaluateIngredient(r, optIngs[i].substring(1));

                    // If the ingredient would contribute to the dish then add it
                    if(Integer.parseInt(eing.substring(0, eing.indexOf('*'))) > 0)
                    {
                        used[ind] = used[ind] + optQs[i].getNum();
                        newIngs.add(eing);
                        newQuantities.add(optQs[i]);
                    }
                }
            }
            // If ingredient is a category
            else
            {
                // Pull the list of ingredients and evaluate each
                ArrayList<Ingredient> cat = Ingredient.getIngredientsByCategory(optIngs[i]);
                String eing = null;
                String bestIng = null;
                int bestInd = -1;
                int best = -8000; // Set to a massive negative number to ensure that an ingredient is selected if
                                  // only avoided ingredients are present

                for(int j = 0; j < cat.size(); j++)
                {
                    int ind = Ingredient.getIndexOfIngredient(cat.get(j).getName());
                    if(quantities[ind] >= optQs[i].getNum() + used[ind])
                    {
                        // Evaluate the goodness of each ingredient
                        eing = evaluateIngredient(r, cat.get(j).getName());
                        int curr = Integer.parseInt(eing.substring(0, eing.indexOf('*')));

                        // If ingredient is the best seen so far save it in the concrete ingredient list
                        if(curr > best)
                        {
                            bestInd = ind;
                            bestIng = eing;
                            best = curr;
                        }
                    }
                }

                if(bestIng != null)
                {
                    // If the ingredient will add to the dish then use it, otherwise ignore
                    if(Integer.parseInt(bestIng.substring(0, bestIng.indexOf('*'))) > 0)
                    {
                        used[bestInd] = used[bestInd] + optQs[i].getNum();
                        newIngs.add(bestIng);
                        newQuantities.add(optQs[i]);
                    }
                }
            }
        }

        // Evaluate the optional subcomponents
        for(int i = 0; i < r.getSubComponents().length; i++)
        {
            if(r.getSubComponents()[i].charAt(0) == '-')
            {
                ArrayList<Recipe> subRecs = getRecipesByCategory(availableRecipes,
                        (r.getSubComponents()[i].substring(1)));
                Recipe best = null;
                int bestScore = -8000;

                for(int j = 0; j < subRecs.size(); j++)
                {
                    Recipe subR = matchIngredients(subRecs.get(j), computeDifferenceArray(quantities, used));
                    int score = 0;

                    if(subR == null)
                    {
                        continue;
                    }
                    else
                    {
                        String[] eings = subR.getIngredients();
                        for(int k = 0; k < eings.length; k++)
                        {
                            score = score + Integer.parseInt(eings[i].substring(0, eings[i].indexOf('*')));
                        }

                        if(score > bestScore)
                        {
                            bestScore = score;
                            best = subR;
                        }
                    }
                }

                // If subcomponent can be made
                if(best != null)
                {
                    // Only use it if it adds something to the dish
                    if(bestScore > 0)
                    {
                        // Add the ingredients to the list of new ingredients, update the used
                        // list, and add the time required for the subcomponent
                        for(int l = 0; l < best.getIngredients().length; l++)
                        {
                            String eing = best.getIngredients()[l];
                            newIngs.add(eing);
                            newQuantities.add(best.getQuantities()[l]);
                            int ind = Ingredient.getIndexOfIngredient(eing.substring(eing.indexOf('*') + 1));
                            used[ind] = used[ind] + best.getQuantities()[l].getNum();
                        }

                        timeNeeded = timeNeeded + best.getTime();
                    }
                }
            }
        }

        return new Recipe(r.getName(), r.getDifficulty(), r.getTime() + timeNeeded, r.getComplexity(),
                r.getCategories(), newIngs.toArray(new String[newIngs.size()]), r.getSubComponents(),
                newQuantities.toArray(new Quantity[newQuantities.size()]), r.getSpicy(), r.getBitter(), r.getPungent(),
                r.getSweet(), r.getUmami(), false);
    }

    private String evaluateIngredient(Recipe r, String ing)
    {
        System.out.println(ing);
        int goodness = 0;
        if(inList(ing, preferredIngredients))
        {
            goodness = goodness + 10;
            System.out.println("Is preferred, +10");
        }
        if(inList(ing, avoidedIngredients))
        {
            goodness = goodness - 200;
            System.out.println("Is avoided, -200");
        }
        if(inList(ing, Competition.getBasket()))
        {
            goodness = goodness + 25;
            System.out.println("Is in the basket, +25");
        }
        int flavorDist = computeFlavorDistance(r, Ingredient.getIngredientByName(ing));
        goodness = goodness - 20 * flavorDist;
        System.out.println("Flavor distance is " + flavorDist + " -" + flavorDist * 20);

        return goodness + "*" + ing;
    }

    private ArrayList<Recipe> sortListByGoodness(ArrayList<Recipe> recipes)
    {
        for(int i = 0; i < recipes.size(); i++)
        {
            int max = i;

            for(int j = i + 1; j < recipes.size(); j++)
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

    public static ArrayList<Recipe> getRecipesByCategory(Recipe[] recs, String category)
    {
        ArrayList<Recipe> catRecs = new ArrayList<Recipe>();

        for(int i = 0; i < recs.length; i++)
        {
            if(inList(category, recs[i].getCategories()))
            {
                catRecs.add(recs[i]);
            }
        }

        return catRecs;
    }

    public static String[] checkCategories(Recipe r)
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

    public static boolean inList(String theme, String[] categories)
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

    private static int[] computeDifferenceArray(int[] quantities, int[] used)
    {
        int[] diff = new int[quantities.length];

        for(int i = 0; i < diff.length; i++)
        {
            diff[i] = quantities[i] - used[i];
        }

        return diff;
    }
}