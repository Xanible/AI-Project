// CSC 416
// Semester Project

//Main class to run the application and set up the user interface

package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame
{

    private static final long serialVersionUID = -3268699488432330183L;
    // Placeholder lists
    private static String[] ingredients = { "Apple", "Bread", "Spinach", "Egg", "Potato", "Celery", "Beef", "Jalapeno",
            "Pineapple", "Tomato", "Pork", "Bacon", "Chicken", "Crackers", "Quail eggs", "Biscuits", "Caviar",
            "Duck eggs", "Whole raw chicken", "Waffles", "Bleu cheese", "Siracha", "Enoki mushrooms", "Pear", "Kiwi",
            "Blueberry", "Poblano pepper", "Cauliflower", "Smoked salmon", "Squid", "Fried rice", "Marshmallows",
            "Corn flakes", "Banana pudding", "Chocolate chips", "Rack of lamb", "Maple syrup", "Sea urchin",
            "Popcorn" };
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
    private ActionListener cbBasketListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent ae)
        {
            JCheckBox eventSrc = (JCheckBox) (ae.getSource());

            if(!(eventSrc.isSelected()))
            {
                currBasket = currBasket - 1;
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
            }
        }
    };
    private ActionListener btnBeginListener = new ActionListener()
    {
        public void actionPerformed(ActionEvent ae)
        {
            if(round < 3)
            {

                txaOutput.append(padString(txaOutput.getRows() / 2 - 3) + "Round " + rounds[round] + "\n");
                round = round + 1;

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

        pnlLeft = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = .2;
        lblBasket = new JLabel("Basket");
        pnlLeft.add(lblBasket, c);

        pnlBasket = new JPanel(new GridLayout(ingredients.length, 1));
        cbBasket = new JCheckBox[ingredients.length];

        for(int i = 0; i < ingredients.length; i++)
        {
            cbBasket[i] = new JCheckBox(ingredients[i]);
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
        ddTheme = new JComboBox<String>(themes);
        pnlTheme.add(ddTheme);
        c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 0.1;
        pnlRight.add(pnlTheme, c);

        btnBegin = new JButton("Begin");
        btnBegin.addActionListener(btnBeginListener);
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

    private String padString(int length)
    {
        String retStr = "";
        int retLength = 0;

        while(retLength < length)
        {
            if(length - retLength >= 8)
            {
                retStr = retStr + "\t";
                retLength = retLength + 8;
            }
            else
            {
                retStr = retStr + " ";
                retLength = retLength + 1;
            }
        }

        return retStr;
    }

    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        new Main();
    }

}
