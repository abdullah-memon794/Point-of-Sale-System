package pos.pro;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class customer extends JPanel {

    private JTabbedPane jTabbedPane1;
    private JTextField c_search, c_name, c_tp, c_bank, c_city;
    private JTextArea c_billadd, c_shipadd;
    private JCheckBox same;
    private JTextField cp_name, c_person, cp_tp, cp_email, cp_online;
    private JButton btnSave, btnSearch, btnUpdate, btnDelete;
    private JTable jTable1;
    private JTextField c_search_tbl, sh_city, sh_tp, sh_cp, sh_pn;
    private JButton btnRefreshSearch; // Added Refresh Button

    public customer() {
        initUI();
        tb_load();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        jTabbedPane1 = new JTabbedPane();
        jTabbedPane1.addTab("Add Customer", createAddCustomerTab());
        jTabbedPane1.addTab("Search Customer", createSearchCustomerTab());
        add(jTabbedPane1, BorderLayout.CENTER);
    }

    private JPanel createAddCustomerTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topSearchPanel.setBorder(BorderFactory.createEtchedBorder());
        topSearchPanel.add(new JLabel("Search ID:"));
        c_search = new JTextField("", 20); // Removed default "0"
        topSearchPanel.add(c_search);
        panel.add(topSearchPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        JPanel basicInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        c_name = new JTextField(); c_tp = new JTextField(); c_bank = new JTextField();
        c_city = new JTextField();
        c_billadd = new JTextArea(3, 20);
        c_shipadd = new JTextArea(3, 20);
        same = new JCheckBox("Same as Billing");

        addField(basicInfoPanel, "Name:", c_name, gbc, 0);
        addField(basicInfoPanel, "T.P Number:", c_tp, gbc, 1);
        gbc.gridx = 0; gbc.gridy = 2;
        basicInfoPanel.add(new JLabel("Billing Address:"), gbc);
        gbc.gridx = 1;
        basicInfoPanel.add(new JScrollPane(c_billadd), gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        JPanel shipPanel = new JPanel(new BorderLayout());
        shipPanel.add(new JLabel("Shipping Address:"), BorderLayout.NORTH);
        shipPanel.add(same, BorderLayout.SOUTH);
        basicInfoPanel.add(shipPanel, gbc);
        gbc.gridx = 1;
        basicInfoPanel.add(new JScrollPane(c_shipadd), gbc);
        addField(basicInfoPanel, "Bank Acc No:", c_bank, gbc, 4);
        addField(basicInfoPanel, "City:", c_city, gbc, 5);

        JPanel contactPanel = new JPanel(new GridBagLayout());
        contactPanel.setBorder(BorderFactory.createTitledBorder("Contact Person"));
        cp_name = new JTextField();
        c_person = new JTextField();
        cp_tp = new JTextField();
        cp_email = new JTextField();
        cp_online = new JTextField();
        addField(contactPanel, "Name:", cp_name, gbc, 0);
        addField(contactPanel, "Contact Person:", c_person, gbc, 1);
        addField(contactPanel, "T.P Number:", cp_tp, gbc, 2);
        addField(contactPanel, "Email:", cp_email, gbc, 3);
        addField(contactPanel, "Online:", cp_online, gbc, 4);
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        contactPanel.add(new JLabel(""), gbc);

        formPanel.add(basicInfoPanel);
        formPanel.add(contactPanel);
        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnSave = new JButton("Save");
        btnSearch = new JButton("Search");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        same.addActionListener(e -> c_shipadd.setText(same.isSelected() ? c_billadd.getText() : ""));
        btnSave.addActionListener(e -> saveCustomer());
        btnSearch.addActionListener(e -> searchCustomerById());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        return panel;
    }

    private JPanel createSearchCustomerTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // --- Top Row of Search ---
        JPanel nameSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameSearchPanel.add(new JLabel("Customer Name:"));
        c_search_tbl = new JTextField("", 20); // Removed default "0"
        nameSearchPanel.add(c_search_tbl);

        // NEW: Refresh Button added here
        btnRefreshSearch = new JButton("Refresh");
        btnRefreshSearch.addActionListener(e -> {
            c_search_tbl.setText("");
            sh_city.setText("");
            sh_tp.setText("");
            sh_cp.setText("");
            sh_pn.setText("");
            tb_load(); // Reload the full table
        });
        nameSearchPanel.add(btnRefreshSearch);

        filterPanel.add(nameSearchPanel);

        // --- Bottom Row of Search ---
        JPanel advancedSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        sh_city = new JTextField(10);
        sh_tp = new JTextField(10);
        sh_cp = new JTextField(10);
        sh_pn = new JTextField(10);
        advancedSearchPanel.add(new JLabel("City:"));
        advancedSearchPanel.add(sh_city);
        advancedSearchPanel.add(new JLabel("Telephone:"));
        advancedSearchPanel.add(sh_tp);
        advancedSearchPanel.add(new JLabel("Contact Person:"));
        advancedSearchPanel.add(sh_cp);
        advancedSearchPanel.add(new JLabel("Person Name:"));
        advancedSearchPanel.add(sh_pn);
        filterPanel.add(advancedSearchPanel);

        panel.add(filterPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Customer Name", "T.P Number", "Billing Address",
                "Shipping Address", "Bank", "City", "Person Name", "Contact Person",
                "Person TP", "Email", "Online"};
        jTable1 = new JTable(new DefaultTableModel(null, columns));
        jTable1.setRowHeight(25);
        panel.add(new JScrollPane(jTable1), BorderLayout.CENTER);

        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int r = jTable1.getSelectedRow();
                c_search.setText(jTable1.getValueAt(r, 0).toString());
                c_name.setText(jTable1.getValueAt(r, 1).toString());
                c_tp.setText(jTable1.getValueAt(r, 2).toString());
            }
        });

        KeyListener searchListener = new KeyAdapter() {
            public void keyReleased(KeyEvent evt) { serch(); }};
        sh_city.addKeyListener(searchListener);
        sh_tp.addKeyListener(searchListener);
        sh_cp.addKeyListener(searchListener);
        sh_pn.addKeyListener(searchListener);
        c_search_tbl.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                try {
                    DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
                    dt.setRowCount(0);
                    ResultSet rs = db.mycon().createStatement().executeQuery("SELECT * FROM customer WHERE customer_name LIKE '%" + c_search_tbl.getText() + "%' ");
                    while (rs.next()) {
                        Vector v = new Vector();
                        for (int i = 1; i <= 12; i++) v.add(rs.getString(i)); dt.addRow(v); }
                } catch (Exception e) { tb_load(); }
            }
        });
        return panel;
    }

    private void addField(JPanel panel, String label, Component field, GridBagConstraints gbc, int y) {
        gbc.gridy = y; gbc.gridx = 0; gbc.weightx = 0.1; panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.9; panel.add(field, gbc);
    }

    public void tb_load() {
        try {
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            dt.setRowCount(0);
            ResultSet rs = db.mycon().createStatement().executeQuery(" SELECT * FROM customer");
            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i <= 12; i++) v.add(rs.getString(i)); dt.addRow(v); }
        } catch (SQLException e) {}
    }

    public void serch() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            ResultSet rs = db.mycon().createStatement().executeQuery(" SELECT * FROM customer WHERE city LIKE '%" + sh_city.getText() + "%' AND tp_number LIKE '%" + sh_tp.getText() + "%' AND contact_person LIKE '%" + sh_cp.getText() + "%' AND person_name LIKE '%" + sh_pn.getText() + "%' ");
            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i <= 12; i++) v.add(rs.getString(i)); dtm.addRow(v); }
        } catch (Exception e) {}
    }

    public void clearText() {
        c_name.setText(""); c_tp.setText(""); c_billadd.setText(""); c_shipadd.setText(""); c_bank.setText(""); c_city.setText("");
        cp_name.setText(""); c_person.setText(""); cp_tp.setText(""); cp_email.setText(""); cp_online.setText("");
    }

    private void saveCustomer() {
        try {
            Statement s = db.mycon().createStatement();
            s.executeUpdate(" INSERT INTO customer (customer_name,Tp_Number,billing_address,shipping_address,bank,city,person_name,contact_person,person_tp,email,online) VALUES ('" + c_name.getText() + "','" + c_tp.getText() + "','" + c_billadd.getText() + "','" + c_shipadd.getText() + "','" + c_bank.getText() + "','" + c_city.getText() + "','" + cp_name.getText() + "','" + c_person.getText() + "','" + cp_tp.getText() + "','" + cp_email.getText() + "','" + cp_online.getText() + "')");
            JOptionPane.showMessageDialog(null, "Data saved");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        tb_load();
        clearText();
    }

    private void updateCustomer() {
        try {
            db.mycon().createStatement().executeUpdate(" UPDATE customer SET customer_name ='" + c_name.getText() + "' ,Tp_Number ='" + c_tp.getText() + "',billing_address ='" + c_billadd.getText() + "',shipping_address ='" + c_shipadd.getText() + "' ,bank ='" + c_bank.getText() + "' ,city ='" + c_city.getText() + "' ,person_name ='" + cp_name.getText() + "' ,contact_person ='" + c_person.getText() + "' ,person_tp ='" + cp_tp.getText() + "' ,email ='" + cp_email.getText() + "' ,online ='" + cp_online.getText() + "'  WHERE cid = '" + c_search.getText() + "' ");
            JOptionPane.showMessageDialog(null, "Data Updated");
        } catch (Exception e) {}
        tb_load(); clearText();
    }

    private void deleteCustomer() {
        try {
            db.mycon().createStatement().executeUpdate("DELETE FROM customer WHERE cid = '" + c_search.getText() + "'");
            JOptionPane.showMessageDialog(null, "Data Deleted");
        } catch (SQLException e) {}
        tb_load(); clearText();
    }

    private void searchCustomerById() {
        try {
            ResultSet rs = db.mycon().createStatement().executeQuery(" SELECT * FROM customer WHERE cid = '" + c_search.getText() + "'");
            if (rs.next()) {
                c_name.setText(rs.getString("customer_name")); c_tp.setText(rs.getString("Tp_Number")); c_billadd.setText(rs.getString("billing_address")); c_shipadd.setText(rs.getString("shipping_address")); c_bank.setText(rs.getString("bank")); c_city.setText(rs.getString("city"));
                cp_name.setText(rs.getString("person_name")); c_person.setText(rs.getString("contact_person")); cp_tp.setText(rs.getString("person_tp")); cp_email.setText(rs.getString("email")); cp_online.setText(rs.getString("online"));
            }
        } catch (SQLException e) {}
    }
}