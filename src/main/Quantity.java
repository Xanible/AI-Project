package main;

public class Quantity
{
    private int num;
    private String type;
    
    public Quantity(int n, String s)
    {
        num = n;
        type = s;
    }
    
    public int getNum()
    {
        return num;
    }
    
    public String getType()
    {
        return type;
    }
}
