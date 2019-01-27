package banking.client;

import banking.Database;
import banking.model.Client;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class ClientIndex extends JPanel {
    private String[][] mData;
    private String[] mColumns;

    public ClientIndex () {
        Connection con = Database.getAppConnection();
        if (con == null) {
            System.out.println("Impossible de se connecter à la base de données");
            return;
        }
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, first_name, last_name from clients");
            ArrayList<Client> clients = new ArrayList<Client>();
            while (rs.next()) {
                Client client = new Client()
                        .setId(rs.getInt("id"))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"));
                clients.add(client);
                mData = new String[clients.size()][3];
                int index = 0;
                for (Client c : clients) {
                    mData[index++] = new String[]{c.getId() + "", c.getFirstName(), c.getLastName()};
                }
                System.out.println(client);
            }
            con.close();
        } catch (Exception e) { e.printStackTrace();}
        mColumns = new String[]{"ID", "First name", "Last name"};
        JTable table = new JTable(mData, mColumns);
        table.setCellSelectionEnabled(false);
        // make horizontal scrollable
        // table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(e -> {
            String selectedData = null;
            int[] selectedRow = table.getSelectedRows();
            int[] selectedColumns = table.getSelectedColumns();
            for (int i1 : selectedRow) {
                for (int selectedColumn : selectedColumns) {
                    selectedData = (String) table.getValueAt(i1, selectedColumn);
                }
            }
            System.out.println("Selected: " + selectedData);
        });
        JScrollPane tableContainer = new JScrollPane(table);
        // Icon addIcon = new ImageIcon("plus.png");
        JButton addButton = new JButton("Ajouter ");
        addButton.addActionListener(actionEvent -> {
            JFrame frame = new JFrame("Ajouter client");
            Container container = frame.getContentPane();
            container.setLayout(new GridLayout(1, 10));
            container.add(new ClientCreate());
            frame.pack();
            frame.setSize(600, 420);
            frame.setVisible(true);
        });
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(addButton);
        add(panel, BorderLayout.NORTH);
        add(tableContainer, BorderLayout.CENTER);
        setVisible(true);
    }
}