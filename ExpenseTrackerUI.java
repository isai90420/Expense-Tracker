package tracker;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class ExpenseTrackerUI extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    // Login fields
    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JTextField emailField = new JTextField(20);

    // Dashboard table
    private DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Type", "Category", "Title", "Description", "Amount", "Method", "Date", "Time"}, 0);
    private JTable table = new JTable(tableModel);
    private JLabel balanceLabel = new JLabel("Balance: 0.00");
    private JLabel totalCreditLabel = new JLabel("Total Credit: 0.00");
    private JLabel totalDebitLabel = new JLabel("Total Debit: 0.00");

    private int loggedInUserId = -1;

    // Currency symbols for background animation
    private static class CurrencySymbol {
        String symbol;
        float x, y, targetX, targetY;
        float alpha, speed;
        Color color;
        int size;
    }

    private CurrencySymbol[] symbols = new CurrencySymbol[30];
    private Random rand = new Random();

    public ExpenseTrackerUI() {
        setTitle("EXPENSE TRACKER");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initSymbols();
        mainPanel.add(buildLoginPanel(), "login");
        mainPanel.add(buildDashboardPanel(), "dashboard");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        setVisible(true);

        // Smooth animation for currency symbols
        new Timer(35, e -> animateSymbols()).start();
    }

    private void initSymbols() {
        String[] currencies = {"₹", "$", "€", "£", "¥", "₩", "₽"};
        for (int i = 0; i < symbols.length; i++) {
            CurrencySymbol s = new CurrencySymbol();
            s.symbol = currencies[rand.nextInt(currencies.length)];
            s.x = rand.nextInt(800);
            s.y = rand.nextInt(600);
            s.targetX = s.x + rand.nextInt(100) - 50;
            s.targetY = s.y + rand.nextInt(100) - 50;
            s.alpha = 0.5f + rand.nextFloat() * 0.5f;
            s.speed = 0.02f + rand.nextFloat() * 0.03f;
            s.size = 25 + rand.nextInt(25);
            s.color = new Color(rand.nextInt(156) + 100, rand.nextInt(156) + 100, rand.nextInt(156) + 100, 180);
            symbols[i] = s;
        }
    }

    private void animateSymbols() {
        for (CurrencySymbol s : symbols) {
            s.x += (s.targetX - s.x) * s.speed;
            s.y += (s.targetY - s.y) * s.speed;
            if (Math.abs(s.x - s.targetX) < 1) s.targetX = rand.nextInt(getWidth());
            if (Math.abs(s.y - s.targetY) < 1) s.targetY = rand.nextInt(getHeight());
        }
        repaint();
    }

    // ----------------------- LOGIN PANEL -----------------------
    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth(), h = getHeight();
                g2d.setPaint(new GradientPaint(0, 0, new Color(255, 230, 230), w, h, new Color(230, 255, 250)));
                g2d.fillRect(0, 0, w, h);

                g2d.setFont(new Font("SansSerif", Font.BOLD, 30));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                for (CurrencySymbol s : symbols) {
                    g2d.setColor(new Color(s.color.getRed(), s.color.getGreen(), s.color.getBlue(), 150));
                    g2d.setFont(new Font("SansSerif", Font.BOLD, s.size));
                    g2d.drawString(s.symbol, (int) s.x, (int) s.y);
                }
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Heading
        JLabel heading = new JLabel("EXPENSE TRACKER", SwingConstants.CENTER);
        heading.setFont(new Font("Poppins", Font.BOLD, 48));
        heading.setForeground(new Color(100, 100, 160));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(heading, gbc);

        gbc.gridwidth = 1;

        // Username
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel userLabel = new JLabel("Username:");
        styleLabel(userLabel);
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        styleField(usernameField);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        styleLabel(passLabel);
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        styleField(passwordField);
        panel.add(passwordField, gbc);

        // Email
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel emailLabel = new JLabel("Email:");
        styleLabel(emailLabel);
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        styleField(emailField);
        panel.add(emailField, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setOpaque(false);
        Color buttonColor = new Color(179, 225, 245);
        JButton loginBtn = createRoundedButton("Login", buttonColor);
        JButton registerBtn = createRoundedButton("Register", buttonColor);
        JButton forgotBtn = createRoundedButton("Forgot Password?", buttonColor);
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        btnPanel.add(forgotBtn);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        loginBtn.addActionListener(e -> loginAction());
        registerBtn.addActionListener(e -> registerAction());
        forgotBtn.addActionListener(e -> forgotPasswordAction());

        return panel;
    }

    private void styleLabel(JLabel label) {
        label.setForeground(new Color(120, 120, 180));
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 18));
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.setBackground(new Color(255, 255, 255, 200));
        field.setOpaque(true);
    }

    private JButton createRoundedButton(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? color.darker() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setForeground(new Color(50, 50, 50));
        btn.setFont(new Font("Poppins", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ----------------------- DASHBOARD PANEL -----------------------
    private JPanel buildDashboardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth(), h = getHeight();
                g2d.setPaint(new GradientPaint(0, 0, new Color(255, 240, 220), w, h, new Color(220, 240, 255)));
                g2d.fillRect(0, 0, w, h);

                g2d.setFont(new Font("SansSerif", Font.BOLD, 25));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                for (CurrencySymbol s : symbols) {
                    g2d.setColor(new Color(s.color.getRed(), s.color.getGreen(), s.color.getBlue(), 150));
                    g2d.setFont(new Font("SansSerif", Font.BOLD, s.size));
                    g2d.drawString(s.symbol, (int) s.x, (int) s.y);
                }
            }
        };
        panel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        JPanel balancePanel = new JPanel();
        balancePanel.setOpaque(false);
        balanceLabel.setForeground(new Color(90, 150, 200));
        totalCreditLabel.setForeground(new Color(120, 200, 150));
        totalDebitLabel.setForeground(new Color(255, 130, 130));
        balancePanel.add(balanceLabel);
        balancePanel.add(totalCreditLabel);
        balancePanel.add(totalDebitLabel);

        topPanel.add(balancePanel);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        Color buttonColor = new Color(179, 225, 245);
        JButton addBtn = createRoundedButton("Add", buttonColor);
        JButton editBtn = createRoundedButton("Edit", buttonColor);
        JButton deleteBtn = createRoundedButton("Delete", buttonColor);
        JButton chartBtn = createRoundedButton("Chart", buttonColor);
        JButton logoutBtn = createRoundedButton("Logout", buttonColor);
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(chartBtn);
        btnPanel.add(logoutBtn);

        topPanel.add(btnPanel);
        panel.add(topPanel, BorderLayout.NORTH);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String type = (String) table.getValueAt(row, 1);
                if ("DEBIT".equalsIgnoreCase(type)) c.setForeground(new Color(255, 100, 100));
                else if ("CREDIT".equalsIgnoreCase(type)) c.setForeground(new Color(100, 200, 100));
                if (isSelected) c.setBackground(new Color(180, 220, 255));
                else c.setBackground(new Color(255, 255, 255, 200));
                return c;
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        logoutBtn.addActionListener(e -> {
            loggedInUserId = -1;
            usernameField.setText("");
            passwordField.setText("");
            emailField.setText("");
            cardLayout.show(mainPanel, "login");
        });

        return panel;
    }

    // ----------------------- ACTION METHODS -----------------------
    private void loginAction() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        int id = UserDAO.login(user, pass);
        if (id != -1) {
            loggedInUserId = id;
            JOptionPane.showMessageDialog(this, "✅ Login successful!");
            showDashboard();
        } else JOptionPane.showMessageDialog(this, "❌ Invalid credentials");
    }

    private void registerAction() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();
        if (user.isEmpty() || pass.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Fill all fields");
            return;
        }
        if (UserDAO.register(user, pass, email))
            JOptionPane.showMessageDialog(this, "✅ Registered! Now login.");
        else
            JOptionPane.showMessageDialog(this, "❌ Registration failed (username/email may exist).");
    }

    private void forgotPasswordAction() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Enter your email");
            return;
        }
        JOptionPane.showMessageDialog(this, "✅ If email exists, reset link sent!");
    }

    private void showDashboard() {
        refreshTable();
        refreshBalance();
        cardLayout.show(mainPanel, "dashboard");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<String[]> txns = TransactionDAO.getTransactions(loggedInUserId);
        for (String[] row : txns) tableModel.addRow(row);
    }

    private void refreshBalance() {
        double credit = 0, debit = 0;
        List<String[]> txns = TransactionDAO.getTransactions(loggedInUserId);
        for (String[] row : txns) {
            double amt = Double.parseDouble(row[5]);
            if ("CREDIT".equalsIgnoreCase(row[1])) credit += amt;
            else debit += amt;
        }
        balanceLabel.setText(String.format("Balance: %.2f", (credit - debit)));
        totalCreditLabel.setText(String.format("Total Credit: %.2f", credit));
        totalDebitLabel.setText(String.format("Total Debit: %.2f", debit));
    }

    // ----------------------- MAIN METHOD -----------------------
    public static void main(String[] args) {
        DBConnection.initDB();
        SwingUtilities.invokeLater(() -> new ExpenseTrackerUI());
    }
}
