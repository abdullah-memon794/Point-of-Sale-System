package pos.pro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class sale extends JPanel {

    public static String cus_id = "0";

    private JLabel inid;
    private JComboBox<String> com_cus, com_pro;
    private JTextField p_qty, paid_amt;
    private JLabel u_price, tot_price, br_code, bill_tot, balance, tot_qty;
    private JTable jTable1;
    private JButton btnAdd, btnRemove, btnRemoveAll, btnPay;

    public sale() {
        initUI();
        data_load();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Font boldFont = new Font("Tahoma", Font.BOLD, 14);

        // --- Top Panel ---
        JPanel topContainer = new JPanel(new BorderLayout(5, 5));
        JPanel invoicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        invoicePanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel lblInvoice = new JLabel("INVOICE NO :");
        lblInvoice.setFont(boldFont);
        inid = new JLabel("01");
        inid.setFont(boldFont);
        invoicePanel.add(lblInvoice);
        invoicePanel.add(inid);
        topContainer.add(invoicePanel, BorderLayout.NORTH);

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        selectionPanel.setBorder(BorderFactory.createEtchedBorder());

        com_cus = new JComboBox<>(new String[]{"select"}); com_cus.setFont(boldFont);
        com_pro = new JComboBox<>(new String[]{"select"}); com_pro.setFont(boldFont);
        p_qty = new JTextField("0", 5); p_qty.setFont(new Font("Tahoma", Font.BOLD, 18));
        u_price = new JLabel("00.00"); u_price.setFont(boldFont);
        tot_price = new JLabel("00.00"); tot_price.setFont(boldFont);
        br_code = new JLabel("0"); br_code.setFont(boldFont);

        selectionPanel.add(new JLabel("Customer :")); selectionPanel.add(com_cus);
        selectionPanel.add(new JLabel("Product :")); selectionPanel.add(com_pro);
        selectionPanel.add(new JLabel("Qty :")); selectionPanel.add(p_qty);
        selectionPanel.add(new JLabel("Unit Price :")); selectionPanel.add(u_price);
        selectionPanel.add(new JLabel("Total Price :")); selectionPanel.add(tot_price);
        selectionPanel.add(br_code);

        topContainer.add(selectionPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // --- Center Panel ---
        JPanel centerContainer = new JPanel(new BorderLayout(10, 10));
        String[] columns = {"INID", "Name", "Bar code", "Qty", "Unit Price", "Total Price"};
        jTable1 = new JTable(new DefaultTableModel(null, columns));
        jTable1.setRowHeight(25);
        centerContainer.add(new JScrollPane(jTable1), BorderLayout.CENTER);

        JPanel cartButtonPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        cartButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        btnAdd = new JButton("Add to Cart");
        btnRemove = new JButton("Remove");
        btnRemoveAll = new JButton("Remove All");
        cartButtonPanel.add(btnAdd); cartButtonPanel.add(btnRemove); cartButtonPanel.add(btnRemoveAll);
        centerContainer.add(cartButtonPanel, BorderLayout.EAST);
        add(centerContainer, BorderLayout.CENTER);

        // --- Bottom Panel ---
        JPanel bottomContainer = new JPanel(new BorderLayout(10, 10));
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statsPanel.setBorder(BorderFactory.createEtchedBorder());

        paid_amt = new JTextField("0", 10); paid_amt.setFont(new Font("Tahoma", Font.BOLD, 18));
        tot_qty = new JLabel("00"); tot_qty.setFont(new Font("Tahoma", Font.BOLD, 14));
        statsPanel.add(new JLabel("Paid Amount :")); statsPanel.add(paid_amt);
        statsPanel.add(new JLabel("Total Qty :")); statsPanel.add(tot_qty);

        JPanel totalsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        totalsPanel.setBorder(BorderFactory.createEtchedBorder());
        totalsPanel.add(new JLabel("Total Amount :"));
        bill_tot = new JLabel("00.00"); bill_tot.setFont(boldFont); totalsPanel.add(bill_tot);
        totalsPanel.add(new JLabel("Balance/Due :"));
        balance = new JLabel("00.00"); balance.setFont(boldFont); totalsPanel.add(balance);
        statsPanel.add(totalsPanel);
        bottomContainer.add(statsPanel, BorderLayout.CENTER);

        btnPay = new JButton("Pay"); // Renamed to just "Pay"
        btnPay.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnPay.setPreferredSize(new Dimension(250, 50));
        JPanel payPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        payPanel.add(btnPay);
        bottomContainer.add(payPanel, BorderLayout.SOUTH);
        add(bottomContainer, BorderLayout.SOUTH);

        // --- Listeners ---
        com_cus.addActionListener(e -> {
            if(com_cus.getSelectedItem() == null) return;
            try {
                ResultSet rs = db.mycon().createStatement().executeQuery("SELECT cid FROM customer WHERE customer_name ='" + com_cus.getSelectedItem() + "'");
                if (rs.next()) cus_id = rs.getString("cid");
            } catch (SQLException ex) {}
        });

        com_pro.addActionListener(e -> {
            if(com_pro.getSelectedItem() == null) return;
            try {
                ResultSet rs = db.mycon().createStatement().executeQuery("SELECT Bar_code,Price FROM product WHERE Product_Name ='" + com_pro.getSelectedItem() + "'");
                if (rs.next()) {
                    u_price.setText(rs.getString("Price"));
                    br_code.setText(rs.getString("Bar_code"));
                }
                pro_tot_cal();
            } catch (SQLException ex) {}
        });

        p_qty.addKeyListener(new KeyAdapter() { public void keyReleased(KeyEvent evt) { pro_tot_cal(); }});
        paid_amt.addKeyListener(new KeyAdapter() { public void keyReleased(KeyEvent evt) { tot(); }});

        btnAdd.addActionListener(e -> {
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            Vector<String> v = new Vector<>();
            v.add(inid.getText()); v.add(com_pro.getSelectedItem().toString());
            v.add(br_code.getText()); v.add(p_qty.getText());
            v.add(u_price.getText()); v.add(tot_price.getText());
            dt.addRow(v); cart_total(); tot();
        });

        btnRemove.addActionListener(e -> {
            int rw = jTable1.getSelectedRow();
            if(rw >= 0) ((DefaultTableModel) jTable1.getModel()).removeRow(rw);
            cart_total(); tot();
        });

        btnRemoveAll.addActionListener(e -> {
            ((DefaultTableModel) jTable1.getModel()).setRowCount(0);
            cart_total(); tot();
        });

        btnPay.addActionListener(e -> checkout());
    }

    public void data_load() {
        try {
            ResultSet rs = db.mycon().createStatement().executeQuery("SELECT customer_name FROM customer");
            Vector<String> v = new Vector<>(); while (rs.next()) v.add(rs.getString(1));
            com_cus.setModel(new DefaultComboBoxModel<>(v));

            rs = db.mycon().createStatement().executeQuery("SELECT Product_Name FROM product");
            v = new Vector<>(); while (rs.next()) v.add(rs.getString(1));
            com_pro.setModel(new DefaultComboBoxModel<>(v));

            rs = db.mycon().createStatement().executeQuery("SELECT val FROM extra WHERE exid = 1");
            if (rs.next()) inid.setText(rs.getString(1));
            inid.setText(String.valueOf(Integer.parseInt(inid.getText()) + 1));
        } catch (Exception e) {}
    }

    public void pro_tot_cal() {
        try { tot_price.setText(String.valueOf(Double.valueOf(p_qty.getText()) * Double.valueOf(u_price.getText()))); }
        catch (Exception e) { tot_price.setText("00.00"); }
    }

    public void cart_total() {
        double total = 0, totals = 0;
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            total += Double.valueOf(jTable1.getValueAt(i, 5).toString());
            totals += Double.valueOf(jTable1.getValueAt(i, 3).toString());
        }
        bill_tot.setText(Double.toString(total)); tot_qty.setText(Double.toString(totals));
    }

    public void tot() {
        try { balance.setText(String.valueOf(Double.valueOf(paid_amt.getText()) - Double.valueOf(bill_tot.getText()))); }
        catch (Exception e) { balance.setText("00.00"); }
    }

    private void checkout() {
        try {
            DefaultTableModel dt = (DefaultTableModel) jTable1.getModel();
            Statement s = db.mycon().createStatement();
            for (int i = 0; i < dt.getRowCount(); i++) {
                s.executeUpdate(" INSERT INTO cart (INID, Product_Name, Bar_code, qty, Unit_Price, Total_Price) VALUES ('"
                        + dt.getValueAt(i, 0) + "','" + dt.getValueAt(i, 1) + "','" + dt.getValueAt(i, 2) + "','"
                        + dt.getValueAt(i, 3) + "','" + dt.getValueAt(i, 4) + "','" + dt.getValueAt(i, 5) + "') ");
            }

            Double tot = Double.valueOf(bill_tot.getText());
            Double pid = Double.valueOf(paid_amt.getText());
            String Status = (pid.equals(0.0)) ? "UnPaid" : (tot > pid) ? "Partial" : "Paid";

            s.executeUpdate("INSERT INTO sales(INID, Cid, Customer_Name, Total_Qty, Total_Bill, Status, Balance) VALUES('"
                    + inid.getText() + "','" + cus_id + "','" + (com_cus.getSelectedItem() != null ? com_cus.getSelectedItem().toString() : "")
                    + "','" + tot_qty.getText() + "','" + bill_tot.getText() + "','" + Status + "','" + balance.getText() + "')");

            s.executeUpdate("UPDATE extra SET val='" + inid.getText() + "' WHERE exid = 1");

            JOptionPane.showMessageDialog(null, "Payment Complete! Cart Saved.");

            // Clear the cart for the next customer
            dt.setRowCount(0);
            cart_total();
            tot();
            data_load(); // generate next invoice number

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}