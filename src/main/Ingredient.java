// CSC 416
// Semester Project

//Ingredient class to store ingredient information

package main;

public class Ingredient {
	
/*
	Ingredient objects have the following information:
		Name as string
		Category as string
*/
	private String name;
	private String category;
	
	public Ingredient(String name, String category)
	{
	    this.name = name;
	    this.category = category;
	}
	
	public String getName()
	{
	    return name;
	}
	
	public String getCategory()
	{
	    return category;
	}
}
