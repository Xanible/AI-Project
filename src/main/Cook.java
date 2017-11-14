// CSC 416
// Semester Project

//Cook class to handle each of the 4 chefs and their dishes

package main;

public class Cook {

/*
	Hard code 4 chefs and their cooking tendencies/preferences such as:
		Name as string
		Skill level as integer 1-5
		Array of preferred ingredients
		Array of avoided ingredients (i.e. allergies)
		Preferred level of complexity as integer
		Array of themes/recipes they’re phenomenal at
	Recipes being used as objects

 */
	private String name;
	private int skill;
	private int preferredComplexity;
	private String[] preferredIngredients;
	private String[] avoidedIngredients;
	private String[] excelAt;
	private int score;
	
	public Cook(String name, int skill, int preferredComplexity, String[] preferred, String[] avoided, String[] excelAt)
	{
	    this.name = name;
	    this.skill = skill;
	    this.preferredComplexity = preferredComplexity;
	    this.preferredIngredients = preferred;
	    this.avoidedIngredients = avoided;
	    this.excelAt = excelAt;
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
	public Recipe getDish()
	{
	    return null;
	}
	
	// Stub - should apply heuristics and claim the best of the remaining pantry ingredients -- need to add list for available ingredients/quantities
	public int[] accessPantry(Ingredient[] pantry, int[] quantities)
	{
	    return quantities;
	}
}