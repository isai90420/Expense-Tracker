package tracker;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new ExpenseTrackerUI());
    }
}
