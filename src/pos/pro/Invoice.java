package pos.pro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Invoice extends JPanel {

    // --- GUI Components ---
    private JTable jTable1;
    private JTextField inid;
    private JTextField cus_name;
    private JComboBox<String> com_status;
    private JButton btnRefresh;

    public Invoice() {
        // Initialize the pure Swing UI
        initUI();

        // Load data from the database
        datalad();
    }

    private void initUI() {
        // Use BorderLayout for the main panel
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ==========================================
        // 1. Top Panel (Search Filters)
        // ==========================================
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBorder(BorderFactory.createEtchedBorder());

        // Font used for all top panel components
        Font boldFont = new Font("Tahoma", Font.BOLD, 14);

        // INID Field (Fixed: Removed the default "0")
        JLabel lblInid = new JLabel("INID :");
        lblInid.setFont(boldFont);
        inid = new JTextField("", 10);
        inid.setFont(boldFont);

        // Customer Name Field (Fixed: Removed the default "0")
        JLabel lblCusName = new JLabel("Customer Name :");
        lblCusName.setFont(boldFont);
        cus_name = new JTextField("", 15);
        cus_name.setFont(boldFont);

        // Status Combo Box (Fixed: Changed blank space to "All" for better User Experience)
        JLabel lblStatus = new JLabel("Status :");
        lblStatus.setFont(boldFont);
        com_status = new JComboBox<>(new String[]{"All", "UnPaid", "Partial", "Paid"});
        com_status.setFont(boldFont);

        // Refresh Button
        btnRefresh = new JButton("Refresh");

        // Add components to top panel
        topPanel.add(lblInid);
        topPanel.add(inid);
        topPanel.add(lblCusName);
        topPanel.add(cus_name);
        topPanel.add(lblStatus);
        topPanel.add(com_status);
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);

        // ==========================================
        // 2. Center Panel (The Table)
        // ==========================================
        String[] columns = {"SaleID", "INID", "CID", "Customer_Name", "Total Qty", "Total Bill", "Status", "Balance"};
        jTable1 = new JTable(new DefaultTableModel(null, columns));
        jTable1.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(jTable1);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());

        add(scrollPane, BorderLayout.CENTER);

        // ==========================================
        // 3. Event Listeners
        // ==========================================

        // Key listener for live search as you type
        KeyAdapter searchTypingListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                search_para();
            }
        };

        inid.addKeyListener(searchTypingListener);
        cus_name.addKeyListener(searchTypingListener);

        // Action listeners for Dropdown and Button
        com_status.addActionListener(e -> search_para());
        btnRefresh.addActionListener(e -> {
            // Clear text fields when refreshing
            inid.setText("");
            cus_name.setText("");
            com_status.setSelectedIndex(0); // Set back to "All"
            datalad();
        });
    }

    // ==========================================================
    // DATABASE AND LOGIC METHODS
    // ==========================================================

    public void datalad() {
        try {
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            dt.setRowCount(0);

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM sales");

            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("saleid"));
                v.add(rs.getString("INID"));
                v.add(rs.getString("Cid"));
                v.add(rs.getString("Customer_Name"));
                v.add(rs.getString("Total_Qty"));
                v.add(rs.getString("Total_Bill"));
                v.add(rs.getString("Status"));
                v.add(rs.getString("Balance"));
                dt.addRow(v);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void search_para() {
        // Use .trim() to accidentally ignore spaces at the end of what you type
        String inv_id = inid.getText().trim();
        String c_Name = cus_name.getText().trim();
        String sta = com_status.getSelectedItem().toString();

        try {
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            dt.setRowCount(0);

            // Step 1: Start building the SQL query based on the text boxes
            String sql = "SELECT * FROM sales WHERE INID LIKE '%" + inv_id + "%' AND Customer_Name LIKE '%" + c_Name + "%'";

            // Step 2: Only filter by status if the user didn't select "All"
            if (!sta.equals("All")) {
                sql = sql + " AND Status = '" + sta + "'";
            }

            Statement s = db.mycon().createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                Vector<String> v = new Vector<>();
                v.add(rs.getString("saleid"));
                v.add(rs.getString("INID"));
                v.add(rs.getString("Cid"));
                v.add(rs.getString("Customer_Name"));
                v.add(rs.getString("Total_Qty"));
                v.add(rs.getString("Total_Bill"));
                v.add(rs.getString("Status"));
                v.add(rs.getString("Balance"));
                dt.addRow(v);
            }

        } catch (SQLException e) {
            System.out.println(e);
            datalad();
        }
    }
}