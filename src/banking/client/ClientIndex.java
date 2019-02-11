package banking.client;

import banking.Database;
import banking.model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class ClientIndex extends JPanel {
    private int mEditingIndex;
    private JButton mEditButton;
    private JButton mDeleteButton;
    private Client mSelectedClient;
    private Class[] mColumnTypes = new Class[]{Long.class, String.class, String.class, Long.class, String.class, Integer.class, Long.class, String.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class};
    private String[] mColumns = new String[]{
            "ID",
            "First name",
            "Last name",
            "Age",
            "Region",
            "Child(s)",
            "Income",
            "Sex",
            "is_married",
            "has_save_act",
            "has_current_act",
            "has_mortgage",
            "has_car",
            "has_pep"
    };
    private DefaultTableModel mTableModel;
    private JTable mTable;
    private ArrayList<Client> mClients = new ArrayList<>();

    public ClientIndex () {
        // mTableModel = new ClientTableModel(mClients);
        mTableModel = new DefaultTableModel(){
            @Override
            public String getColumnName (int i) {
                return mColumns[i];
            }

            @Override
            public int getColumnCount () {
                return mColumns.length;
            }

            @Override
            public Class<?> getColumnClass (int column) {
                return mColumnTypes[column];
            }

            @Override
            public boolean isCellEditable (int row, int column) {
                return false;
            }
        };
        mTable = new JTable(mTableModel);
        Connection con = Database.getAppConnection();
        if (con == null) {
            System.out.println("Impossible de se connecter à la base de données");
            return;
        }
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from clients");
            while (rs.next()) {
                Client client = new Client()
                        .setId(rs.getInt("id"))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"))
                        .setAge(rs.getInt("age"))
                        .setRegion(rs.getString("region"))
                        .setChildCount(rs.getInt("child_count"))
                        .setIncome(rs.getInt("income"))
                        .setSex(rs.getString("sex"))
                        .setIsMarried(rs.getBoolean("is_married"))
                        .setHasSaveAct(rs.getBoolean("has_save_act"))
                        .setHasCurrentAct(rs.getBoolean("has_current_act"))
                        .setHasMortgage(rs.getBoolean("has_mortgage"))
                        .setHasCar(rs.getBoolean("has_car"))
                        .setHasPep(rs.getBoolean("has_pep"))
                        ;
                mClients.add(client);
                addClient(client);
            }
            con.close();
        } catch (Exception e) { e.printStackTrace();}
        // make horizontal scrollable
        mTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mTable.setCellSelectionEnabled(false);
        mTable.setRowSelectionAllowed(true);
        JScrollPane tableContainer = new JScrollPane(mTable);
        // Icon addIcon = new ImageIcon("plus.png");
        mEditButton = setUpEditButton();
        JButton addButton = setUpAddButton();
        mDeleteButton = setUpDeleteButton();
        mTable.getSelectionModel().addListSelectionListener(event -> {
            mEditingIndex = mTable.getSelectedRow();
            boolean isSelected = mEditingIndex > -1;
            mEditButton.setVisible(isSelected);
            mDeleteButton.setVisible(isSelected);
            if (isSelected) {
                mSelectedClient = getClientFromRow(mEditingIndex);
            }
        });
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(mEditButton);
        panel.add(mDeleteButton);
        panel.add(addButton);
        add(panel, BorderLayout.NORTH);
        add(tableContainer, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton setUpAddButton () {
        JButton button = new JButton("Ajouter ");
        button.addActionListener(actionEvent -> {
            JFrame frame = new JFrame("Ajouter client");
            Container container = frame.getContentPane();
            container.setLayout(new GridLayout(1, 10));
            ClientCreate clientCreate = new ClientCreate();
            clientCreate.setOnClientAddedListener(client -> {
                addClient(client);
                // TODO: yes/no close dialog
                frame.setVisible(false);
            });
            container.add(clientCreate);
            frame.pack();
            frame.setSize(600, 420);
            frame.setVisible(true);
        });
        return button;
    }

    private JButton setUpEditButton () {
        JButton button = new JButton("Editer ");
        button.setVisible(false);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent actionEvent) {
                JFrame frame = new JFrame("Editer client");
                Container container = frame.getContentPane();
                container.setLayout(new GridLayout(1, 10));
                ClientEdit clientEdit = new ClientEdit(mSelectedClient);
                clientEdit.setOnClientSubmittedListener(c -> {
                    updateClient(c);
                    // TODO: yes/no close dialog
                    mTable.clearSelection();
                    mSelectedClient = null;
                    button.setVisible(false);
                    frame.setVisible(false);
                });
                container.add(clientEdit);
                frame.pack();
                frame.setSize(600, 420);
                frame.setVisible(true);
            }
        });
        return button;
    }

    private JButton setUpDeleteButton () {
        JButton button = new JButton("Supprimer ");
        button.setVisible(false);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent actionEvent) {
                int dialogResult = JOptionPane.showConfirmDialog(ClientIndex.this,
                        String.format("Voulez-vous vraiment supprimer le client %s", mSelectedClient.getFullName()),
                        "Warning",
                        JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION){
                    deleteClient(mTable.getSelectedRow());
                }
            }
        });
        return button;
    }

    private void deleteClient (int row) {
        Connection con = Database.getAppConnection();
        if (con == null) {
            System.out.println("Impossible de se connecter à la base de données");
            return;
        }
        try {
            PreparedStatement statement = con.prepareStatement("DELETE FROM clients WHERE id = ?");
            statement.setInt(1, mSelectedClient.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
            JOptionPane.showMessageDialog(null, "Client supprimé.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        mTableModel.removeRow(row);
        mTable.clearSelection();
        mSelectedClient = null;
        mDeleteButton.setVisible(false);
    }

    public void updateClient (Client client) {
        mTableModel.setValueAt(client.getId(), mEditingIndex, 0);
        mTableModel.setValueAt(client.getFirstName(), mEditingIndex, 1);
        mTableModel.setValueAt(client.getLastName(), mEditingIndex, 2);
        mTableModel.setValueAt(client.getAge(), mEditingIndex, 3);
        mTableModel.setValueAt(client.getRegion(), mEditingIndex, 4);
        mTableModel.setValueAt(client.getChildCount(), mEditingIndex, 5);
        mTableModel.setValueAt(client.getIncome(), mEditingIndex, 6);
        mTableModel.setValueAt(client.getSex(), mEditingIndex, 7);
        mTableModel.setValueAt(client.isMarried(), mEditingIndex, 8);
        mTableModel.setValueAt(client.hasSaveAct(), mEditingIndex, 9);
        mTableModel.setValueAt(client.hasCurrentAct(), mEditingIndex, 10);
        mTableModel.setValueAt(client.hasMortgage(), mEditingIndex, 11);
        mTableModel.setValueAt(client.hasCar(), mEditingIndex, 12);
        mTableModel.setValueAt(client.hasPep(), mEditingIndex, 13);
    }


    private Client getClientFromRow (int row) {
        return new Client()
            .setId((Integer) mTable.getValueAt(row, 0))
            .setFirstName((String) mTable.getValueAt(row, 1))
            .setLastName((String) mTable.getValueAt(row, 2))
            .setAge((Integer) mTable.getValueAt(row, 3))
            .setRegion((String) mTable.getValueAt(row, 4))
            .setChildCount((Integer) mTable.getValueAt(row, 5))
            .setIncome((Long) mTable.getValueAt(row, 6))
            .setSex((String) mTable.getValueAt(row, 7))
            .setIsMarried((Boolean) mTable.getValueAt(row, 8))
            .setHasSaveAct((Boolean) mTable.getValueAt(row, 9))
            .setHasCurrentAct((Boolean) mTable.getValueAt(row, 10))
            .setHasMortgage((Boolean) mTable.getValueAt(row, 11))
            .setHasCar((Boolean) mTable.getValueAt(row, 12))
            .setHasPep((Boolean) mTable.getValueAt(row, 13));
    }


    public void addClient (Client client) {
        Vector row = new Vector();
        row.add(client.getId());
        row.add(client.getFirstName());
        row.add(client.getLastName());
        row.add(client.getAge());
        row.add(client.getRegion());
        row.add(client.getChildCount());
        row.add(client.getIncome());
        row.add(client.getSex());
        row.add(client.isMarried());
        row.add(client.hasSaveAct());
        row.add(client.hasCurrentAct());
        row.add(client.hasMortgage());
        row.add(client.hasCar());
        row.add(client.hasPep());
        mTableModel.addRow(row);
    }
}