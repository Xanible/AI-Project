// CSC 416
// Semester Project

//Main class to run the application and set up the user interface

/*
	Main Class should:
		Set up the user interface
		Set basket ingredients
		Set round themes
		Initialize everything
 */

package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class Main extends JFrame
{

    private static final long serialVersionUID = -3268699488432330183L;
    private static final String delimiter = "   ";

    // Placeholder lists
    private static String[] themes = { "Breakfast", "Lunch", "Dinner", "Brunch", "Dessert", "Asian", "Italian",
            "Mexican", "Healthy", "Vegetarian", "Sandwiches", "Pastas", "Appetizer", "Tacos" };

    private String[] rounds = { "One", "Two", "Three" };
    private int round;
    private JLabel lblBasket;
    private JCheckBox[] cbBasket;
    private JScrollPane scpBasket, scpOutput;
    private JTextArea txaOutput;
    private JLabel lblTheme;
    private JComboBox<String> ddTheme;
    private JButton btnBegin;
    private JPanel pnlLeft, pnlBasket, pnlRight, pnlTheme;
    private int currBasket;
    private Competition comp;
    private ActionListener cbBasketListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent ae)
        {
            JCheckBox eventSrc = (JCheckBox) (ae.getSource());

            if(!(eventSrc.isSelected()))
            {
                currBasket = currBasket - 1;
                if(currBasket == 3)
                {
                    btnBegin.setEnabled(false);
                }
            }
            else if(currBasket == 4)
            {
                eventSrc.setSelected(false);
                txaOutput.append("The basket is full.\n");
            }
            else
            {
                eventSrc.setSelected(true);
                currBasket = currBasket + 1;
                if(currBasket == 4)
                {
                    btnBegin.setEnabled(true);
                }
            }
        }
    };
    private ActionListener btnBeginListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent ae)
        {
            if(round < 3)
            {

                txaOutput.append("\t\t    Round " + rounds[round] + "\n");
                round = round + 1;

                txaOutput.append(comp.beginRound(getCurrentBasket(), (String) (ddTheme.getSelectedItem())));

                if(round == 3)
                {
                    btnBegin.setEnabled(false);
                }
            }
        }
    };

    public Main()
    {
        super("AI Chopped");
        setLayout(new GridBagLayout());

        round = 0;
        comp = new Competition();

        pnlLeft = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = .2;
        lblBasket = new JLabel("Basket");
        pnlLeft.add(lblBasket, c);

        pnlBasket = new JPanel(new GridLayout(Ingredient.ingredients.length, 1));
        cbBasket = new JCheckBox[Ingredient.ingredients.length];

        for(int i = 0; i < Ingredient.ingredients.length; i++)
        {
            cbBasket[i] = new JCheckBox(Ingredient.ingredients[i].getName());
            cbBasket[i].addActionListener(cbBasketListener);
            cbBasket[i].setMinimumSize(new Dimension(200, 20));
            pnlBasket.add(cbBasket[i]);
        }

        scpBasket = new JScrollPane(pnlBasket);
        scpBasket.setMinimumSize(new Dimension(300, 760));
        scpBasket.getVerticalScrollBar().setUnitIncrement(10);
        scpBasket.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scpBasket.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = .8;
        c.gridy = 1;
        pnlLeft.add(scpBasket, c);

        txaOutput = new JTextArea(200, 800);
        txaOutput.setEditable(false);
        txaOutput.setLineWrap(true);
        scpOutput = new JScrollPane(txaOutput);
        scpOutput.getVerticalScrollBar().setUnitIncrement(10);
        scpBasket.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scpBasket.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        pnlRight = new JPanel(new GridBagLayout());

        pnlTheme = new JPanel(new GridLayout(1, 2));
        lblTheme = new JLabel("Theme:");
        pnlTheme.add(lblTheme);
        ddTheme = new JComboBox<String>(Competition.themes);
        pnlTheme.add(ddTheme);
        c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 0.1;
        pnlRight.add(pnlTheme, c);

        btnBegin = new JButton("Begin");
        btnBegin.addActionListener(btnBeginListener);
        btnBegin.setEnabled(false);
        c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 0.2;
        c.gridy = 1;
        c.insets = new Insets(500, 0, 0, 0);
        pnlRight.add(btnBegin, c);

        c = new GridBagConstraints();
        c.weightx = .20;
        c.weighty = 1;
        add(pnlLeft, c);

        c = new GridBagConstraints();
        c.weightx = .65;
        c.weighty = 1;
        c.gridx = 1;
        c.fill = GridBagConstraints.BOTH;
        add(scpOutput, c);

        c = new GridBagConstraints();
        c.weightx = .15;
        c.weighty = 1;
        c.gridx = 2;
        add(pnlRight, c);

        setSize(1200, 800);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private String[] getCurrentBasket()
    {
        String[] basket = new String[4];
        int inBasket = 0;

        for(int i = 0; i < cbBasket.length; i++)
        {
            if(cbBasket[i].isSelected())
            {
                basket[inBasket] = cbBasket[i].getText();
                inBasket = inBasket + 1;
            }
        }

        return basket;
    }

    public static void main(String[] args) throws Exception
    {
        readIngredientsFile();
        readRecipesFile();

        new Main();
    }

    public static void readIngredientsFile() throws Exception
    {
        BufferedReader in = new BufferedReader(new FileReader(new File("ingredients.txt")));
        ArrayList<Ingredient> ings = new ArrayList<Ingredient>();

        // Discard the first line
        in.readLine();

        while(in.ready())
        {
            String input = in.readLine();
            String[] parts = input.split(delimiter);

            parts[0] = parts[0].replaceAll("_", " ");
            parts[1] = parts[1].replaceAll("_", " ");

            ings.add(new Ingredient(parts[1], parts[0], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6])));
        }

        Ingredient.ingredients = ings.toArray(new Ingredient[ings.size()]);
        in.close();
    }

    public static void readRecipesFile() throws Exception
    {
        BufferedReader in = new BufferedReader(new FileReader(new File("recipes.txt")));
        ArrayList<Recipe> recs = new ArrayList<Recipe>();
        ArrayList<String> themes = new ArrayList<String>();

        // Discard the first line
        in.readLine();

        while(in.ready())
        {
            // Read in the name, difficulty, time required, and complexity of the recipe
            String input = in.readLine();
            String[] parts = input.split(delimiter);

            String name = parts[0].replaceAll("_", " ");
            int diff = Integer.parseInt(parts[1]);
            int time = Integer.parseInt(parts[2]);
            int complex = Integer.parseInt(parts[3]);

            // Read in the categories of the recipe
            input = in.readLine();
            parts = input.split(delimiter);

            String[] cats = new String[parts.length - 1];

            for(int i = 1; i < parts.length; i++)
            {
                if(!(themes.contains(parts[i])))
                {
                    themes.add(parts[i]);
                }
                cats[i - 1] = parts[i];
            }

            // Read in the ingredients (optional and required)
            input = in.readLine();
            parts = input.split(delimiter);

            ArrayList<String> ings = new ArrayList<String>();
            ArrayList<String> optIngs = new ArrayList<String>();

            for(int i = 1; i < parts.length; i++)
            {
                if(parts[i].charAt(0) == '-')
                {
                    optIngs.add(parts[i]);
                }
                else
                {
                    ings.add(parts[i]);
                }
            }

            // Read in the quantities
            input = in.readLine();
            parts = input.split(delimiter);

            Quantity[] quants = new Quantity[parts.length - 1];

            for(int i = 1; i < parts.length; i++)
            {
                String[] subparts = parts[i].split("_");

                quants[i - 1] = new Quantity(Integer.parseInt(subparts[0]), subparts[1]);
            }

            recs.add(new Recipe(name, diff, time, complex, cats, ings.toArray(new String[ings.size()]),
                    optIngs.toArray(new String[optIngs.size()]), quants));

            // Discard separator
            in.readLine();
        }

        Competition.themes = themes.toArray(new String[themes.size()]);
        Recipe.recipes = recs.toArray(new Recipe[recs.size()]);
        in.close();
    }
}