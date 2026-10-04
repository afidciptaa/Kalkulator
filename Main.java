import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry point for the Modern Java Desktop Calculator.
 */
public class Main {
    public static void main(String[] args) {
        // High-DPI and font rendering optimization
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Try setting system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Launch UI on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            CalculatorUI calculator = new CalculatorUI();
            calculator.show();
        });
    }
}
