package pos.pro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class product extends JPanel {

    // --- GUI Components ---
    private JTextField p_src, p_name, p_bcode, p_price, p_qty, p_sid, c_search_tbl;
    private JButton btnSave, btnSearch, btnUpdate, btnDelete;
    private JTable jTable1;

    public product() {
        // Initialize the pure Swing UI
        initUI();

        // Load data from the database
        tb_load();
    }

    private void initUI() {
        // Split the screen into two halves (Left: Form, Right: Table)
        setLayout(new GridLayout(1, 2, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ==========================================
        // LEFT SIDE: Product Form & Buttons
        // ==========================================
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));

        // 1. Top Left: Search by ID Panel
        JPanel searchIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchIdPanel.setBorder(BorderFactory.createTitledBorder("Product Info"));
        searchIdPanel.add(new JLabel("Search ID :"));
        p_src = new JTextField("0", 15);
        p_src.setFont(new Font("Tahoma", Font.BOLD, 14));
        searchIdPanel.add(p_src);
        leftPanel.add(searchIdPanel, BorderLayout.NORTH);

        // 2. Center Left: Data Entry Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEtchedBorder());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;

        p_name = new JTextField("0");
        p_bcode = new JTextField("0");
        p_price = new JTextField("0");
        p_qty = new JTextField("0");
        p_sid = new JTextField("0");

        // Helper method to add fields nicely
        addField(formPanel, "Name :", p_name, gbc, 0);
        addField(formPanel, "Bar Code :", p_bcode, gbc, 1);
        addField(formPanel, "Price :", p_price, gbc, 2);
        addField(formPanel, "Qty :", p_qty, gbc, 3);
        addField(formPanel, "Supplier ID :", p_sid, gbc, 4);

        // Push everything up
        gbc.gridy = 5; gbc.weighty = 1.0;
        formPanel.add(new JLabel(""), gbc);
        leftPanel.add(formPanel, BorderLayout.CENTER);

        // 3. Bottom Left: Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnSave = createButton("Save", "/pos/pro/img/save.png");
        btnSearch = createButton("Search", "/pos/pro/img/search x30.png");
        btnUpdate = createButton("Update", "/pos/pro/img/update.png");
        btnDelete = createButton("Delete", "/pos/pro/img/delete.png");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ==========================================
        // RIGHT SIDE: Live Search & Table
        // ==========================================
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        // Top Right: Live Search bar
        JPanel liveSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        liveSearchPanel.add(new JLabel("Search (By Name) :"));
        c_search_tbl = new JTextField("0", 20);
        c_search_tbl.setFont(new Font("Tahoma", Font.BOLD, 14));
        liveSearchPanel.add(c_search_tbl);
        rightPanel.add(liveSearchPanel, BorderLayout.NORTH);

        // Center Right: The Table
        String[] columns = {"ID", "Product Name", "Bar Code", "Price", "Qty", "SID"};
        jTable1 = new JTable(new DefaultTableModel(null, columns));
        jTable1.setRowHeight(25);
        jTable1.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(jTable1);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Add both halves to the main layout
        add(leftPanel);
        add(rightPanel);

        // ==========================================
        // EVENT LISTENERS
        // ==========================================

        btnSave.addActionListener(e -> saveProduct());
        btnSearch.addActionListener(e -> searchProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());

        // Live Search as user types
        c_search_tbl.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                searchTable();
            }
        });

        // Click row to fill form
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int r = jTable1.getSelectedRow();
                p_src.setText(jTable1.getValueAt(r, 0).toString());
                p_name.setText(jTable1.getValueAt(r, 1).toString());
                p_bcode.setText(jTable1.getValueAt(r, 2).toString());
                p_price.setText(jTable1.getValueAt(r, 3).toString());
                p_qty.setText(jTable1.getValueAt(r, 4).toString());
                p_sid.setText(jTable1.getValueAt(r, 5).toString());
            }
        });
    }

    // Helper method to align form fields
    private void addField(JPanel panel, String label, JTextField field,
                          GridBagConstraints gbc, int y) {
        gbc.gridy = y;
        gbc.gridx = 0; gbc.weightx = 0.2;
        JLabel jl = new JLabel(label);
        jl.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(jl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        field.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.add(field, gbc);
    }

    // Helper method to create standard buttons with icons
    private JButton createButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Tahoma", Font.BOLD, 12));
        try {
            java.net.URL imgURL = getClass().getResource(iconPath);
            if (imgURL != null) btn.setIcon(new ImageIcon(imgURL));
        } catch (Exception ex) {}
        return btn;
    }

    // ==========================================================
    // DATABASE AND LOGIC METHODS
    // ==========================================================

    public void tb_load() {
        try {
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            dt.setRowCount(0);

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery(" SELECT * FROM product");

            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
                dt.addRow(v);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void saveProduct() {
        String name = p_name.getText();
        String bcode = p_bcode.getText();
        String price = p_price.getText();
        String qty = p_qty.getText();
        String sid = p_sid.getText();

        try {
            Statement s = db.mycon().createStatement();
            s.executeUpdate("INSERT INTO product (Product_Name,Bar_code,Price,Qty,Sid) VALUES ('" + name + "','" + bcode + "','" + price + "','" + qty + "','" + sid + "')");
            JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException e) {
            System.out.println(e);
        }
        tb_load();
    }

    private void searchProduct() {
        String search = p_src.getText();
        try {
            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery(" SELECT * FROM product WHERE pid ='" + search + "'  ");

            if (rs.next()) {
                p_name.setText(rs.getString("Product_Name"));
                p_bcode.setText(rs.getString("Bar_code"));
                p_price.setText(rs.getString("Price"));
                p_qty.setText(rs.getString("Qty"));
                p_sid.setText(rs.getString("Sid"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private void updateProduct() {
        String id = p_src.getText();
        String name = p_name.getText();
        String bcode = p_bcode.getText();
        String price = p_price.getText();
        String qty = p_qty.getText();
        String sid = p_sid.getText();

        try {
            Statement s = db.mycon().createStatement();
            s.executeUpdate("UPDATE product SET Product_Name='" + name + "',Bar_code='" + bcode + "' ,Price='" + price + "',Qty='" + qty + "',Sid='" + sid + "' WHERE pid ='" + id + "' ");
            JOptionPane.showMessageDialog(null, "Data Updated");
        } catch (Exception e) {
            System.out.println(e);
        }
        tb_load();
    }

    private void deleteProduct() {
        String id = p_src.getText();
        try {
            Statement s = db.mycon().createStatement();
            s.executeUpdate("DELETE FROM product WHERE pid = '" + id + "' ");
            JOptionPane.showMessageDialog(null, "Data Deleted");
        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }
        tb_load();
    }

    private void searchTable() {
        String name = c_search_tbl.getText();
        try {
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            dt.setRowCount(0);
            Statement s = db.mycon().createStatement();

            ResultSet rs = s.executeQuery("SELECT * FROM product WHERE Product_Name LIKE '%" + name + "%' ");

            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString(1));
                v.add(rs.getString(2));
                v.add(rs.getString(3));
                v.add(rs.getString(4));
                v.add(rs.getString(5));
                v.add(rs.getString(6));
                dt.addRow(v);
            }
        } catch (Exception e) {
            tb_load();
        }
    }
}