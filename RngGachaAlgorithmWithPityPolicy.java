import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RngGachaAlgorithmWithPityPolicy {
    private static Random rand = new Random();
    private static byte pityCounter5Star = 0;  // Counter for 5-star pity (90 pulls)
    private static byte pityCounter4Star = 0;  // Counter for 4-star pity (10 pulls)
    private static byte totalPulls = 0;   // Total number of pulls made
    private static JLabel resultLabel;
    private static JLabel pityCounterLabel;

    public static void main(String[] args) {
        // Create the frame (main window)
        JFrame frame = new JFrame("RNG Gacha With Pity algorithm");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350);
        frame.setLayout(new BorderLayout());

        // Create a panel to hold the button and the labels
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create a label to display the pull result
        resultLabel = new JLabel("Welcome to the Gacha Pull Simulator,Test your luck here!");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a label to show the pity counter for both 5-star and 4-star
        pityCounterLabel = new JLabel("5-Star Pity Counter: 0 | 4-Star Pity Counter: 0");
        pityCounterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add the labels to the panel
        panel.add(resultLabel);
        panel.add(pityCounterLabel);

        // Create a "Pull" button (single pull)
        JButton pullButton = new JButton("Pull");
        pullButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pullButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = generateResult(); // Generate the result of the pull
                resultLabel.setText("Pull Result: " + result);  // Update the result label
                pityCounterLabel.setText("5-Star Pity Counter: " + pityCounter5Star + " | 4-Star Pity Counter: " + pityCounter4Star);  // Update the pity counter labels
            }
        });

        // Create a "Pull 10 Times" button
        JButton pull10TimesButton = new JButton("Pull 10 Times");
        pull10TimesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pull10TimesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder results = new StringBuilder("<html>");
                for (int i = 0; i < 10; i++) {
                    results.append("Pull " + (i + 1) + ": " + generateResult() + "<br>");
                }
                results.append("</html>");
                resultLabel.setText(results.toString());  // Show all the results of the 10 pulls
                pityCounterLabel.setText("5-Star Pity Counter: " + pityCounter5Star + " | 4-Star Pity Counter: " + pityCounter4Star);  // Update the pity counter labels
            }
        });

        // Add the buttons to the panel
        panel.add(pullButton);
        panel.add(pull10TimesButton);

        // Add the panel to the frame
        frame.add(panel, BorderLayout.CENTER);

        // Display the window
        frame.setVisible(true);
    }

    // This function simulates the RNG roll
    public static String generateResult() {
        totalPulls++; // Increment total pulls counter

        // Check if we need to guarantee a 5-star result after 89 pulls without a 5-star
        if (pityCounter5Star >= 89) {
            pityCounter5Star = 0; // Reset pity counter after a guaranteed 5-star result
            pityCounter4Star = 0; // Reset 4-star pity counter as well
            return "You got Guaranteed 5-Star item"; // Return a guaranteed 5-star
        }

        // Check if we need to guarantee a 4-star result after 9 pulls without a 4-star
        if (pityCounter4Star >= 9) {
            pityCounter4Star = 0; // Reset pity counter after a guaranteed 4-star result
            pityCounter5Star++; // Increase the 5-star pity counter for next possible 5-star
            return "You got Guaranteed 4-Star item"; // Return a guaranteed 4-star
        }

        // Soft pity logic for increasing chances of 5-star starting at pity 75
        double roll = rand.nextDouble() * 100; // Random number between 0 and 100

        // Soft pity logic for 5-star items (increases probability after pity 75)
        if (pityCounter5Star >= 75) {
            double softPityChance = 0.6 + (pityCounter5Star - 75) * 2;  // Incrementing chance for 5-star
            if (roll < softPityChance) {
                pityCounter5Star = 0; // Reset pity counter on 5-star pull
                pityCounter4Star = 0; // Reset 4-star pity counter as well
                return "You got 5 Star item (Soft Pity)";
            }
        }

        if (roll < 0.6) { // 5-star rarity (0.6%)
            pityCounter5Star = 0; // Reset pity counter on 5-star pull
            pityCounter4Star = 0; // Reset 4-star pity counter as well
            return "You got 5 Star item";
        }

        // Soft pity logic for 4-star items (increases probability after pity 5)
        if (pityCounter4Star >= 5) {
            double softPity4StarChance = 5.4 + (pityCounter4Star - 6) * 15;  // Incrementing chance for 4-star
            if (roll < softPity4StarChance) {
                pityCounter4Star = 0; // Reset 4-star pity counter
                pityCounter5Star++; // Increase the 5-star pity counter
                return "You got 4 Star item (Soft Pity)";
            }
        }

        if (roll < 6.0) { // 4-star rarity (5.4%)
            pityCounter4Star = 0; // Reset 4-star pity counter on 4-star pull
            pityCounter5Star++; // Increase 5-star pity counter
            return "You got 4 Star item";
        } else { // 3-star rarity (94%)
            pityCounter4Star++; // Increase 4-star pity counter
            pityCounter5Star++; // Increase 5-star pity counter
            return "You got 3 Star item";
        }
    }
}
