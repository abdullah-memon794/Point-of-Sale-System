package pos.pro;

import javax.swing.*;
import java.awt.*;

public class Home extends JFrame {

    JpanelLoader jpload = new JpanelLoader();

    private ButtonGroup home_bnt_grp;
    private JToggleButton btnCustomer, btnSupplier, btnProduct, btnEmployee, btnSales, btnInvoice;
    private JPanel panel_load;

    public Home() {
        setTitle("Point of Sale System - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEtchedBorder());
        topPanel.setPreferredSize(new Dimension(0, 60));
        // Corporate Blue background for the top bar
        topPanel.setBackground(Color.decode("#1E3A8A"));
        add(topPanel, BorderLayout.NORTH);

        // --- MAIN CENTER PANEL ---
        panel_load = new JPanel(new BorderLayout());
        panel_load.setBorder(BorderFactory.createEtchedBorder());
        // Crisp Off-White background for the main area to provide clean contrast
        panel_load.setBackground(Color.decode("#F8FAFC"));

        // ==========================================
        // Center Welcome Message added here
        // ==========================================
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        // Ensure the inner panel matches the main area background
        welcomePanel.setBackground(Color.decode("#F8FAFC"));

        // Using basic HTML to format the text nicely!
        JLabel welcomeLabel = new JLabel(
                "<html><div style='text-align: center; color: #444444;'>"
                        + "<h1 style='font-size: 36px; margin-bottom: 20px; color: #0F172A;'>Welcome to the Point of Sale System</h1>"
                        + "<p style='font-size: 18px; color: #666666;'>Proudly created and developed by:</p>"
                        + "<h2 style='font-size: 28px; color: #1E3A8A;'>Abdullah Memon</h2>"
                        + "</div></html>");

        welcomePanel.add(welcomeLabel);
        panel_load.add(welcomePanel, BorderLayout.CENTER);
        // ==========================================

        add(panel_load, BorderLayout.CENTER);

        // --- SIDE NAVIGATION PANEL ---
        JPanel sidePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 40));
        sidePanel.setBorder(BorderFactory.createEtchedBorder());
        sidePanel.setPreferredSize(new Dimension(200, 0));
        // Corporate Blue background for the side bar
        sidePanel.setBackground(Color.decode("#1E3A8A"));

        JPanel buttonContainer = new JPanel(new GridLayout(6, 1, 0, 10));
        buttonContainer.setPreferredSize(new Dimension(170, 300));
        // Ensure the button container matches the side bar
        buttonContainer.setBackground(Color.decode("#1E3A8A"));

        home_bnt_grp = new ButtonGroup();

        btnCustomer = createMenuButton("Customers", "/pos/pro/img/customer.png");
        btnSupplier = createMenuButton("Supplier", "/pos/pro/img/supplier.png");
        btnEmployee = createMenuButton("Employee", "/pos/pro/img/emp.png");
        btnProduct = createMenuButton("Product", "/pos/pro/img/product.png");
        btnSales = createMenuButton("Sales", "/pos/pro/img/sales_menu.png");
        btnInvoice = createMenuButton("Invoice", "/pos/pro/img/invo.png");

        // Action Listeners to load panels
        btnCustomer.addActionListener(e -> jpload.jPanelLoader(panel_load, new customer()));
        btnSupplier.addActionListener(e -> jpload.jPanelLoader(panel_load, new supplier()));
        btnEmployee.addActionListener(e -> jpload.jPanelLoader(panel_load, new employee()));
        btnProduct.addActionListener(e -> jpload.jPanelLoader(panel_load, new product()));
        btnSales.addActionListener(e -> jpload.jPanelLoader(panel_load, new sale()));
        btnInvoice.addActionListener(e -> jpload.jPanelLoader(panel_load, new Invoice()));

        buttonContainer.add(btnCustomer);
        buttonContainer.add(btnSupplier);
        buttonContainer.add(btnEmployee);
        buttonContainer.add(btnProduct);
        buttonContainer.add(btnSales);
        buttonContainer.add(btnInvoice);

        sidePanel.add(buttonContainer);
        add(sidePanel, BorderLayout.WEST);
    }

    private JToggleButton createMenuButton(String text, String iconPath) {
        JToggleButton btn = new JToggleButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        // Keep the background crisp white
        btn.setBackground(Color.decode("#FFFFFF"));

        // Make the text match your Corporate Blue theme!
        btn.setForeground(Color.decode("#1E3A8A"));

        btn.setFocusPainted(false); // Removes the default focus outline when clicked

        try {
            java.net.URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) btn.setIcon(new ImageIcon(imgURL));
        } catch (Exception ex) {
            System.out.println("Icon not found: " + iconPath);
        }
        home_bnt_grp.add(btn);
        return btn;
    }
}