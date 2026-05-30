package pos.pro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class employee extends JPanel {

    private JTabbedPane jTabbedPane1;
    private JTextField c_search, c_name, c_tp, c_bank, c_city;
    private JTextArea c_billadd, c_shipadd;
    private JCheckBox same;
    private JButton btnSave, btnSearch, btnUpdate, btnDelete;
    private JTable jTable1;
    private JTextField c_search_tbl, sh_city, sh_tp;
    private JButton btnRefreshSearch; // Added Refresh Button

    public employee() {
        initUI();
        tb_load();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        jTabbedPane1 = new JTabbedPane();
        jTabbedPane1.addTab("Add Employee", createAddEmployeeTab());
        jTabbedPane1.addTab("Search Employee", createSearchEmployeeTab());
        add(jTabbedPane1, BorderLayout.CENTER);
    }

    private JPanel createAddEmployeeTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Top Search Panel ---
        JPanel topSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topSearchPanel.setBorder(BorderFactory.createEtchedBorder());
        topSearchPanel.add(new JLabel("Search ID:"));
        c_search = new JTextField("", 20); // Removed default "0"
        topSearchPanel.add(c_search);
        panel.add(topSearchPanel, BorderLayout.NORTH);

        // --- Main Info Form ---
        JPanel formPanel = new JPanel(new BorderLayout());
        JPanel basicInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;

        c_name = new JTextField(); c_tp = new JTextField(); c_bank = new JTextField(); c_city = new JTextField();
        c_billadd = new JTextArea(3, 20); c_shipadd = new JTextArea(3, 20); same = new JCheckBox("Same as Main Address");

        addField(basicInfoPanel, "Employee Name:", c_name, gbc, 0);
        addField(basicInfoPanel, "T.P Number:", c_tp, gbc, 1);

        gbc.gridx = 0; gbc.gridy = 2; basicInfoPanel.add(new JLabel("Main Address:"), gbc);
        gbc.gridx = 1; basicInfoPanel.add(new JScrollPane(c_billadd), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JPanel shipPanel = new JPanel(new BorderLayout());
        shipPanel.add(new JLabel("Temp Address:"), BorderLayout.NORTH);
        shipPanel.add(same, BorderLayout.SOUTH);
        basicInfoPanel.add(shipPanel, gbc);
        gbc.gridx = 1; basicInfoPanel.add(new JScrollPane(c_shipadd), gbc);

        addField(basicInfoPanel, "Bank Acc No:", c_bank, gbc, 4);
        addField(basicInfoPanel, "City:", c_city, gbc, 5);

        // Push everything to the top
        gbc.gridy = 6; gbc.weighty = 1.0; basicInfoPanel.add(new JLabel(""), gbc);

        formPanel.add(basicInfoPanel, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.CENTER);

        // --- Action Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnSave = new JButton("Save"); btnSearch = new JButton("Search"); btnUpdate = new JButton("Update"); btnDelete = new JButton("Delete");
        buttonPanel.add(btnSave); buttonPanel.add(btnSearch); buttonPanel.add(btnUpdate); buttonPanel.add(btnDelete);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Listeners ---
        same.addActionListener(e -> c_shipadd.setText(same.isSelected() ? c_billadd.getText() : ""));
        btnSave.addActionListener(e -> saveEmployee());
        btnSearch.addActionListener(e -> searchEmployeeById());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());

        return panel;
    }

    private JPanel createSearchEmployeeTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Search Filters ---
        JPanel filterPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // --- Top Row of Search ---
        JPanel nameSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameSearchPanel.add(new JLabel("Employee Name:"));
        c_search_tbl = new JTextField("", 20); // Removed default "0"
        nameSearchPanel.add(c_search_tbl);

        // NEW: Refresh Button added here
        btnRefreshSearch = new JButton("Refresh");
        btnRefreshSearch.addActionListener(e -> {
            c_search_tbl.setText("");
            sh_city.setText("");
            sh_tp.setText("");
            tb_load(); // Reload the full table
        });
        nameSearchPanel.add(btnRefreshSearch);

        filterPanel.add(nameSearchPanel);

        // --- Bottom Row of Search ---
        JPanel advancedSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        sh_city = new JTextField(15); sh_tp = new JTextField(15);
        advancedSearchPanel.add(new JLabel("City:")); advancedSearchPanel.add(sh_city);
        advancedSearchPanel.add(new JLabel("Telephone:")); advancedSearchPanel.add(sh_tp);

        JButton btnEdit = new JButton("Edit");
        btnEdit.addActionListener(e -> {
            int r = jTable1.getSelectedRow();
            if (r >= 0) {
                c_search.setText(jTable1.getValueAt(r, 0).toString());
                c_name.setText(jTable1.getValueAt(r, 1).toString());
                c_tp.setText(jTable1.getValueAt(r, 2).toString());
                c_billadd.setText(jTable1.getValueAt(r, 3).toString());
                c_shipadd.setText(jTable1.getValueAt(r, 4).toString());
                c_bank.setText(jTable1.getValueAt(r, 5).toString());
                c_city.setText(jTable1.getValueAt(r, 6).toString());
                jTabbedPane1.setSelectedIndex(0);
            }
        });
        advancedSearchPanel.add(btnEdit);
        filterPanel.add(advancedSearchPanel);
        panel.add(filterPanel, BorderLayout.NORTH);

        // --- Employee Table ---
        String[] columns = {"ID", "Employee Name", "T.P Number", "Main Address", "Temp Address", "Bank", "City"};
        jTable1 = new JTable(new DefaultTableModel(null, columns));
        jTable1.setRowHeight(25);
        panel.add(new JScrollPane(jTable1), BorderLayout.CENTER);

        // --- Table & Search Listeners ---
        KeyListener searchListener = new KeyAdapter() { public void keyReleased(KeyEvent evt) { serch(); }};
        sh_city.addKeyListener(searchListener);
        sh_tp.addKeyListener(searchListener);

        c_search_tbl.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                try {
                    DefaultTableModel dt = (DefaultTableModel) jTable1.getModel(); dt.setRowCount(0);
                    // Explicitly select only the columns we need
                    ResultSet rs = db.mycon().createStatement().executeQuery("SELECT eid, Employee_Name, Tp_Number, main_address, temp_address, bank, city FROM employee WHERE Employee_Name LIKE '%" + c_search_tbl.getText() + "%' ");
                    while (rs.next()) {
                        Vector v = new Vector();
                        for (int i = 1; i <= 7; i++) v.add(rs.getString(i));
                        dt.addRow(v);
                    }
                } catch (Exception e) { tb_load(); }
            }
        });
        return panel;
    }

    private void addField(JPanel panel, String label, Component field, GridBagConstraints gbc, int y) {
        gbc.gridy = y; gbc.gridx = 0; gbc.weightx = 0.1; panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.9; panel.add(field, gbc);
    }

    // ==========================================================
    // DATABASE AND LOGIC METHODS
    // ==========================================================

    public void tb_load() {
        try {
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel(); dt.setRowCount(0);
            ResultSet rs = db.mycon().createStatement().executeQuery("SELECT eid, Employee_Name, Tp_Number, main_address, temp_address, bank, city FROM employee");
            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i <= 7; i++) v.add(rs.getString(i));
                dt.addRow(v);
            }
        } catch (SQLException e) {}
    }

    public void serch() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel(); dtm.setRowCount(0);
            ResultSet rs = db.mycon().createStatement().executeQuery("SELECT eid, Employee_Name, Tp_Number, main_address, temp_address, bank, city FROM employee WHERE city LIKE '%" + sh_city.getText() + "%' AND Tp_Number LIKE '%" + sh_tp.getText() + "%'");
            while (rs.next()) {
                Vector v = new Vector();
                for (int i = 1; i <= 7; i++) v.add(rs.getString(i));
                dtm.addRow(v);
            }
        } catch (Exception e) {}
    }

    public void clearText() {
        c_name.setText(""); c_tp.setText(""); c_billadd.setText(""); c_shipadd.setText(""); c_bank.setText(""); c_city.setText("");
    }

    private void saveEmployee() {
        try {
            db.mycon().createStatement().executeUpdate("INSERT INTO employee (Employee_Name, Tp_Number, main_address, temp_address, bank, city) VALUES ('" + c_name.getText() + "','" + c_tp.getText() + "','" + c_billadd.getText() + "','" + c_shipadd.getText() + "','" + c_bank.getText() + "','" + c_city.getText() + "')");
            JOptionPane.showMessageDialog(null, "Employee Data saved successfully");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        tb_load(); clearText();
    }

    private void updateEmployee() {
        try {
            db.mycon().createStatement().executeUpdate("UPDATE employee SET Employee_Name ='" + c_name.getText() + "', Tp_Number ='" + c_tp.getText() + "', main_address ='" + c_billadd.getText() + "', temp_address ='" + c_shipadd.getText() + "', bank ='" + c_bank.getText() + "', city ='" + c_city.getText() + "' WHERE eid = '" + c_search.getText() + "' ");
            JOptionPane.showMessageDialog(null, "Employee Data Updated");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        tb_load(); clearText();
    }

    private void deleteEmployee() {
        try {
            db.mycon().createStatement().executeUpdate("DELETE FROM employee WHERE eid = '" + c_search.getText() + "'");
            JOptionPane.showMessageDialog(null, "Employee Data Deleted");
        } catch (SQLException e) {}
        tb_load(); clearText();
    }

    private void searchEmployeeById() {
        try {
            ResultSet rs = db.mycon().createStatement().executeQuery("SELECT * FROM employee WHERE eid = '" + c_search.getText() + "'");
            if (rs.next()) {
                c_name.setText(rs.getString("Employee_Name"));
                c_tp.setText(rs.getString("Tp_Number"));
                c_billadd.setText(rs.getString("main_address"));
                c_shipadd.setText(rs.getString("temp_address"));
                c_bank.setText(rs.getString("bank"));
                c_city.setText(rs.getString("city"));
            }
        } catch (SQLException e) {}
    }
}