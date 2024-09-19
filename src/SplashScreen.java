import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {

    public SplashScreen() {
        // Set up the splash screen
        setTitle("Splash Screen");
        setSize(1300, 900);
        setMinimumSize(new Dimension(800, 600)); // minimum size
        setUndecorated(true);  // Removes the title bar
        setLocationRelativeTo(null);  // Center the window on the screen

        // Set background image (replace "your-image.jpg" with the path to your image)
        JLabel backgroundLabel = new JLabel(new ImageIcon("src/resources/splashscreen.jpg"));
        backgroundLabel.setLayout(new BorderLayout());  // Enable layout manager on JLabel

        // Create a text label
        JLabel textLabel = new JLabel("Loading... Please wait", SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 44));  // Set font size and style
        textLabel.setForeground(Color.BLUE);  // Set text color

        // Add the text label to the center of the background label
        backgroundLabel.add(textLabel, BorderLayout.CENTER);

        // Add the background label to the frame
        add(backgroundLabel);

        // Make the splash screen visible
        setVisible(true);

        // Simulate loading for 3 seconds, then load the MainDashboard
        new Timer(3000, e -> {
            // Close the splash screen
            dispose();

            // Open the MainDashboard
            SwingUtilities.invokeLater(() -> new MainDashboard());
            // stop the timer after it triggers once
            ((Timer) e.getSource()).stop();

        }).start();
    }

    public static void main(String[] args) {
        // Show the splash screen on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new SplashScreen());
    }
}
