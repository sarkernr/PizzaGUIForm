import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrust, regularCrust, deepDish;
    private JComboBox<String> sizeBox;
    private JCheckBox[] toppings;
    private JTextArea receiptArea;
    private JButton orderButton, clearButton, quitButton;
    private final double[] sizePrices = {8.00, 12.00, 16.00, 20.00};
    private final double TAX_RATE = 0.07;

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(500, 500);

        // Crust Panel
        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(new TitledBorder("Crust Type"));
        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDish = new JRadioButton("Deep-dish");
        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDish);
        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDish);

        // Size Panel
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(new TitledBorder("Size"));
        sizeBox = new JComboBox<>(new String[]{"Small ($8)", "Medium ($12)", "Large ($16)", "Super ($20)"});
        sizePanel.add(sizeBox);

        // Toppings Panel
        JPanel toppingsPanel = new JPanel();
        toppingsPanel.setBorder(new TitledBorder("Toppings ($1 each)"));
        toppingsPanel.setLayout(new GridLayout(3, 2));
        String[] toppingNames = {"Pepperoni", "Mushrooms", "Onions", "Sausage", "Bacon", "Extra Cheese"};
        toppings = new JCheckBox[toppingNames.length];
        for (int i = 0; i < toppings.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            toppingsPanel.add(toppings[i]);
        }

        // Receipt Panel
        JPanel receiptPanel = new JPanel();
        receiptPanel.setBorder(new TitledBorder("Order Receipt"));
        receiptArea = new JTextArea(10, 30);
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        receiptPanel.add(scrollPane);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        // Add panels to frame
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(toppingsPanel, BorderLayout.CENTER);
        centerPanel.add(receiptPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add event listeners
        orderButton.addActionListener(new OrderListener());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> confirmExit());

        setVisible(true);
    }

    private class OrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            generateReceipt();
        }
    }

    private void generateReceipt() {
        // Ensure a crust is selected
        if (!thinCrust.isSelected() && !regularCrust.isSelected() && !deepDish.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select a crust type.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get crust selection
        String crust = thinCrust.isSelected() ? "Thin" : regularCrust.isSelected() ? "Regular" : "Deep-dish";
        int sizeIndex = sizeBox.getSelectedIndex();
        double basePrice = sizePrices[sizeIndex];

        // Get toppings selection
        StringBuilder toppingList = new StringBuilder();
        int toppingCount = 0;
        for (JCheckBox topping : toppings) {
            if (topping.isSelected()) {
                toppingList.append(topping.getText()).append("\n");
                toppingCount++;
            }
        }
        if (toppingCount == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one topping.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calculate costs
        double toppingCost = toppingCount * 1.00;
        double subTotal = basePrice + toppingCost;
        double tax = subTotal * TAX_RATE;
        double total = subTotal + tax;

        // Display receipt
        receiptArea.setText(
                "=========================================\n" +
                        "Type of Crust & Size: " + crust + " & " + sizeBox.getSelectedItem() + "\n" +
                        "Ingredient Price:\n" + toppingList.toString() +
                        "-----------------------------------------\n" +
                        String.format("Sub-total: $%.2f\n", subTotal) +
                        String.format("Tax: $%.2f\n", tax) +
                        "-----------------------------------------\n" +
                        String.format("Total: $%.2f\n", total) +
                        "=========================================\n"
        );
    }

    private void clearForm() {
        // Reset selections
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepDish.setSelected(false);
        sizeBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        receiptArea.setText("");
    }

    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
