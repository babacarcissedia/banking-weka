package banking.client;

import banking.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ClientCreate extends JPanel {
    private final JCheckBox mSaveActField;
    private final JCheckBox mCurrentActField;
    private final JCheckBox mMortageField;
    private final JCheckBox mPepField;
    private JPanel mContainer;
    private JCheckBox mCarField;
    private JTextField mChildCountField;
    private JCheckBox mMarriedField;
    private JTextField mFirstNameField;
    private JTextField mLastNameField;
    private JTextField mAgeField;
    private JTextField mRegionField;
    private JTextField mIncomeField;
    private JComboBox mSexeField;
    private String mSex;
    private JButton mOkButton;
    private JButton mResetButton;
    private boolean mMarried;
    private boolean mCar;
    private boolean mSaveAct;
    private boolean mCurrentAct;
    private boolean mMortage;
    private boolean mPep;


    public ClientCreate () {
        mContainer = new JPanel();
        mContainer.setLayout(new BoxLayout(mContainer, BoxLayout.Y_AXIS));
        add(mContainer, BorderLayout.CENTER);

        mFirstNameField = addField("First name");
        mLastNameField = addField("Last name");
        mAgeField = addField("Age");
        mRegionField = addField("Region");
        mChildCountField = addField("Child count");
        mIncomeField = addField("Income");
        mSexeField = addComboField("Sex", new String[]{"Male", "Female"}, event -> mSex = (String)event.getItem());
        JPanel checkboxes = new JPanel(new GridLayout(3, 3));
        mMarriedField = addFieldBoolean("Married ?", event -> mMarried = event.getStateChange() == ItemEvent.SELECTED);
        mCarField = addFieldBoolean("has car ?", event -> mCar = event.getStateChange() == ItemEvent.SELECTED);
        mSaveActField = addFieldBoolean("has save act ?", event -> mSaveAct = event.getStateChange() == ItemEvent.SELECTED);
        mCurrentActField = addFieldBoolean("has current act ?", event -> mCurrentAct = event.getStateChange() == ItemEvent.SELECTED);
        mMortageField = addFieldBoolean("has mortgage ?", event -> mMortage = event.getStateChange() == ItemEvent.SELECTED);
        mPepField = addFieldBoolean("has pep ?", event -> mPep = event.getStateChange() == ItemEvent.SELECTED);
        checkboxes.add(mMarriedField);
        checkboxes.add(mCarField);
        checkboxes.add(mSaveActField);
        checkboxes.add(mCurrentActField);
        checkboxes.add(mMortageField);
        checkboxes.add(mPepField);
        mContainer.add(checkboxes);

        JPanel controlPanel = new JPanel(new FlowLayout());
        mOkButton = new JButton("Ok");
        mOkButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent actionEvent) {
                addClient();
            }
        });
        mResetButton = new JButton("Reset");
        mResetButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent actionEvent) {
                resetFields();
            }
        });
        controlPanel.add(mOkButton);
        controlPanel.add(mResetButton);
        controlPanel.setVisible(true);
        mContainer.add(controlPanel);

        setVisible(true);
    }

    private void resetFields () {
        mFirstNameField.setText("");
        mLastNameField.setText("");
        mAgeField.setText("");
    }

    private void addClient () {
        Connection con = Database.getAppConnection();
        if (con == null) {
            System.out.println("Impossible de se connecter à la base de données");
            return;
        }
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO clients (age,sex,region,income,married,children,car,save_act,current_act,mortgage,pep) VALUES (:age, :sex, :region, :income, :married, :children, :car, :save_act, :current_act, :mortgage, :pep)");
            statement.setString(Integer.parseInt(mAgeField.getText()), "age");
//            statement.setString(mSex, "sex");
//            statement.setString(mCar, "sex");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JTextField addField (String name) {
        JPanel container = new JPanel(new GridLayout(1,2));
        JLabel label = new JLabel(name, JLabel.LEFT);
        JTextField field = new JTextField(20);
        container.add(label);
        container.add(field);
        mContainer.add(container);
        return field;
    }

    private JCheckBox addFieldBoolean (String name, ItemListener listener) {
        JCheckBox field = new JCheckBox(name);
        field.addItemListener(listener);
        // add(field);
        return field;
    }

    private JComboBox addComboField (String name, String[] values, ItemListener listener) {
        JPanel container = new JPanel(new GridLayout(1,2));
        JLabel label = new JLabel(name);
        JComboBox field = new JComboBox(values);
        field.setEditable(false);
        field.addItemListener(listener);
        container.add(label);
        container.add(field);
        mContainer.add(container);
        return field;
    }
}