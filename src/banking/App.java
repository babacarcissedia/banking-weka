package banking;

import banking.client.ClientIndex;
import banking.model.Client;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class App extends JFrame {
    private Container mContainer;
    private JPanel mElementsContainer;
    private JMenuBar mMenuBar;
    private ArrayList<Client> mClients = new ArrayList<>();

    public App (String title) {
        setTitle(title);
        setSize(800, 1200);
        mContainer = getContentPane();
        mContainer.setLayout(new BorderLayout());
        mElementsContainer = new JPanel(new GridLayout(20, 1));
        mElementsContainer.add(new ClientIndex());

        setUpMenuBar();
        mContainer.add(BorderLayout.NORTH, mMenuBar);
        mContainer.add(BorderLayout.CENTER, mElementsContainer);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing (WindowEvent windowEvent) { System.exit(0); }
        });
        pack();
    }


    private void setUpMenuBar () {
        mMenuBar = new JMenuBar();
        mMenuBar.add(setUpFileMenu());
        mMenuBar.add(Box.createHorizontalGlue());
        mMenuBar.add(setUpHelpMenu());
    }

    private JMenu setUpFileMenu () {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        // JMenuItem openItem = new JMenuItem("Open");
        // openItem.addActionListener(actionEvent -> {});
        JMenuItem importItem = new JMenuItem("Import", KeyEvent.VK_I);
        importItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent actionEvent) {
                onImport();
            }
        });
        JMenuItem exportItem = new JMenuItem("Export", KeyEvent.VK_E);
        exportItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent actionEvent) {
                onExport();
            }
        });
        // fileMenu.add(openItem);
        fileMenu.add(importItem);
        fileMenu.add(exportItem);
        return fileMenu;
    }

    private JMenu setUpHelpMenu () {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.getAccessibleContext().setAccessibleDescription("Help menu");
        helpMenu.add(new JMenuItem("About"));
        return helpMenu;
    }

    private void onImport () {
        //Create a file chooser
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept (File file) {
                String extension = FileUtils.getExtension(file);
                return extension != null && extension.toLowerCase().equals("csv");
            }

            @Override
            public String getDescription () {
                return "CSV files only";
            }
        });
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Opening: " + selectedFile.getAbsoluteFile());
            try {
                FileReader fileReader = new FileReader(selectedFile.getAbsoluteFile());
                fileReader.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onExport () {
        //Create a file chooser
        final JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getAbsolutePath();
            String extension = FileUtils.getExtension(selectedFile);
            if (extension == null || !extension.toLowerCase().equals("csv")) {
                fileName += ".csv";
            }
            System.out.println("Opening: " + fileName);
            try {
                FileWriter fileWriter = new FileWriter(new File(fileName));
                getClientFromDB();
                ArrayList<String> lineValues = new ArrayList<>(Arrays.asList("id",
                        "first_name",
                        "last_name",
                        "age",
                        "region",
                        "child_count",
                        "income",
                        "sex",
                        "is_married",
                        "has_save_act",
                        "has_current_act",
                        "has_mortgage",
                        "has_car",
                        "has_pep"));
                CSVUtils.writeLine(fileWriter, lineValues);
                for (Client client: mClients) {
                    lineValues = new ArrayList<>();
                    lineValues.add(String.valueOf(client.getId()));
                    lineValues.add(client.getFirstName());
                    lineValues.add(client.getLastName());
                    lineValues.add(String.valueOf(client.getAge()));
                    lineValues.add(client.getRegion());
                    lineValues.add(String.valueOf(client.getChildCount()));
                    lineValues.add(String.valueOf(client.getIncome()));
                    lineValues.add(client.getSex());
                    lineValues.add(String.valueOf(client.isMarried()));
                    lineValues.add(String.valueOf(client.hasSaveAct()));
                    lineValues.add(String.valueOf(client.hasCurrentAct()));
                    lineValues.add(String.valueOf(client.hasMortgage()));
                    lineValues.add(String.valueOf(client.hasCar()));
                    lineValues.add(String.valueOf(client.hasPep()));
                    CSVUtils.writeLine(fileWriter, lineValues);
                }
                fileWriter.flush();
                fileWriter.close();
                JOptionPane.showMessageDialog(this,
                        String.format("base de données clients exporté dans \"%s\"", fileName));
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        String.format("Erreur lors de l'exportation de la base de données clients vers le fichier \"%s\"", fileName));
            }
        }
    }

    private void getClientFromDB () {
        Connection con = Database.getAppConnection();
        if (con == null) {
            JOptionPane.showMessageDialog(this, "Impossible de se connecter à la base de données");
            return;
        }
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clients");
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
            }
            con.close();
        } catch (Exception e) { e.printStackTrace();}
    }

    public static int parseInt (String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            System.out.println(String.format("Could not parse %s to int. returning 0.", text));
            return 0;
        }
    }
}