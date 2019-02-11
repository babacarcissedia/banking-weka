package banking.client;

import banking.Database;
import banking.model.Client;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class ClientIndex extends JPanel {
    protected String mLearningFile = "/mnt/myfol/workspaces/M2INFO/data_mining/TP/weka_files/apprentissage.arff";
    protected String mTestFile = "/mnt/myfol/workspaces/M2INFO/data_mining/TP/weka_files/test.arff";
    private int mEditingIndex;
    private JButton mEditButton;
    private JButton mDeleteButton;
    private JButton mPredictButton;
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
        mTableModel = new DefaultTableModel() {
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
                        .setIncome(rs.getLong("income"))
                        .setSex(rs.getString("sex"))
                        .setIsMarried(rs.getBoolean("is_married"))
                        .setHasSaveAct(rs.getBoolean("has_save_act"))
                        .setHasCurrentAct(rs.getBoolean("has_current_act"))
                        .setHasMortgage(rs.getBoolean("has_mortgage"))
                        .setHasCar(rs.getBoolean("has_car"))
                        .setHasPep(rs.getBoolean("has_pep"));
                mClients.add(client);
                addClient(client);
            }
            con.close();
        } catch (Exception e) { e.printStackTrace();}
        // make horizontal scrollable
        //mTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mTable.setCellSelectionEnabled(false);
        mTable.setRowSelectionAllowed(true);
        JScrollPane tableContainer = new JScrollPane(mTable);
        // Icon addIcon = new ImageIcon("plus.png");
        mEditButton = setUpEditButton();
        JButton addButton = setUpAddButton();
        mDeleteButton = setUpDeleteButton();
        mPredictButton = setUpPredictButton();
        mTable.getSelectionModel().addListSelectionListener(event -> {
            mEditingIndex = mTable.getSelectedRow();
            boolean isSelected = mEditingIndex > -1;
            mEditButton.setVisible(isSelected);
            mDeleteButton.setVisible(isSelected);
            mPredictButton.setVisible(isSelected);
            if (isSelected) {
                mSelectedClient = getClientFromRow(mEditingIndex);
            }
        });
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(mEditButton);
        panel.add(mDeleteButton);
        panel.add(mPredictButton);
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
                clientEdit.setOnClientUpdatedListener(c -> {
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
                if (dialogResult == JOptionPane.YES_OPTION) {
                    deleteClient(mTable.getSelectedRow());
                }
            }
        });
        return button;
    }

    private JButton setUpPredictButton () {
        JButton button = new JButton("Prédire ");
        // button.setVisible(false);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent actionEvent) {
                setUpPredictClient(mTable.getSelectedRow());
            }
        });
        return button;
    }


    private void setUpPredictClient (int row) {
        Client client = null;
        if (row > -1) {
            client = getClientFromRow(row);
        }
        ClientForm clientForm = new ClientForm(client, "Prédire");
        clientForm.setOnClientSubmittedListener(c -> {
            try {
                if (predictClient(c)) {
                    JOptionPane.showMessageDialog(this, "Desole! le client ne peut pas avoir un prêt");
                } else {
                    JOptionPane.showMessageDialog(this, "Le client peut avoir un prêt!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de la prédiction: " + e.getMessage());
            }
        });
        JFrame frame = new JFrame();
        Container container = frame.getContentPane();
        container.setLayout(new GridLayout(1, 10));
        frame.pack();
        frame.setSize(600, 420);
        frame.setVisible(true);
        container.add(clientForm);
        frame.pack();
        frame.setVisible(true);
    }

    private boolean predictClient (Client client) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(mLearningFile));
        Instances train = new Instances(reader);
        train.setClassIndex(train.numAttributes() - 1);
        ArrayList<String> trancheAge = new ArrayList<String>(3);
        trancheAge.add("<34.33");
        trancheAge.add("34.33-50.66");
        trancheAge.add(">50.66");
        Attribute age = new Attribute("age", trancheAge);

        ArrayList<String> sex = new ArrayList<String>(2);
        sex.add("MALE");
        sex.add("FEMALE");
        Attribute sexe = new Attribute("sex", sex);

        ArrayList<String> regions = new ArrayList<String>(4);
        regions.add("INNER_CITY");
        regions.add("TOWN");
        regions.add("RURAL");
        regions.add("SUBURBAN");
        Attribute region = new Attribute("region", regions);

        ArrayList<String> revenus = new ArrayList<String>(3);
        revenus.add("<24386.17");
        revenus.add("24386.17-43758.13");
        revenus.add(">43758.13");
        Attribute income = new Attribute("income", revenus);

        ArrayList<String> situation = new ArrayList<String>(2);
        situation.add("NO");
        situation.add("YES");
        Attribute married = new Attribute("married", situation);

        ArrayList<String> child = new ArrayList<String>(4);
        child.add("0");
        child.add("1");
        child.add("2");
        child.add("3");
        Attribute children = new Attribute("children", child);

        ArrayList<String> hascar = new ArrayList<String>(2);
        hascar.add("NO");
        hascar.add("YES");
        Attribute car = new Attribute("car", hascar);

        ArrayList<String> act = new ArrayList<String>(2);
        act.add("NO");
        act.add("YES");
        Attribute save_act = new Attribute("save_act", act);

        ArrayList<String> c_act = new ArrayList<String>(2);
        c_act.add("NO");
        c_act.add("YES");
        Attribute current_act = new Attribute("current_act", c_act);

        ArrayList<String> mortgag = new ArrayList<String>(2);
        mortgag.add("NO");
        mortgag.add("YES");
        Attribute mortgage = new Attribute("mortgage", mortgag);

        ArrayList<String> prets = new ArrayList<String>(2);
        prets.add("NO");
        prets.add("YES");
        Attribute pret = new Attribute("pret", prets);

        reader = new BufferedReader(new FileReader(mTestFile));
        Instances test = new Instances(reader);
        test.setClassIndex(train.numAttributes() - 1);

        Instance clientInstance = test.instance(test.numInstances() - 1);
        clientInstance.setValue(0, getNominalAgeFrom(client));
        clientInstance.setValue(1, client.getSex());
        clientInstance.setValue(2, client.getRegion());
        clientInstance.setValue(3, getNominalIncomeFrom(client));
        clientInstance.setValue(4, client.isMarried() ? "YES" : "NO");
        clientInstance.setValue(5, String.valueOf(client.getChildCount()));
        clientInstance.setValue(6, client.hasCar() ? "YES" : "NO");
        clientInstance.setValue(7, client.hasSaveAct() ? "YES" : "NO");
        clientInstance.setValue(8, client.hasCurrentAct() ? "YES" : "NO");
        clientInstance.setValue(9, client.hasMortgage() ? "YES" : "NO");
        clientInstance.setValue(10, "NO");

        // Print the instance
        System.out.println("The instance: " + clientInstance);
        test.add(clientInstance);
        ArffSaver savefile = new ArffSaver();
        savefile.setInstances(test);
        savefile.setFile(new File(mLearningFile));
        savefile.writeBatch();
        reader.close();
        J48 classifieur = new J48();
        classifieur.buildClassifier(train);
        Instances labeled = new Instances(test);
        double clsLabel = classifieur.classifyInstance(test.instance(test.numInstances() - 1));
        String valeurPredite = test.classAttribute().value((int) clsLabel);

        return valeurPredite.equals("YES");
    }

    private String getNominalIncomeFrom (Client client) {
        long clientIncome = client.getIncome();
        if (clientIncome < 24386) {
            return "<24386.17";
        } else if (clientIncome < 43758) {
            return "24386.17-43758.13";
        } else {
            return ">43758.13";
        }
    }

    private String getNominalAgeFrom (Client client) {
        int client_age = client.getAge();
        if (client_age < 34) {
            return "<34.33";
        } else if (client_age < 50) {
            return "34.33-50.66";
        } else {
            return ">50.66";
        }
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