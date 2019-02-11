package banking.client;

import banking.App;
import banking.model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ClientForm extends JPanel {
    private boolean mIsEdit = false;
    protected String mResetLabel;
    protected String mOkLabel;
    private JTextField mIdField;
    protected JCheckBox mSaveActField;
    protected JCheckBox mCurrentActField;
    protected JCheckBox mMortageField;
    protected JCheckBox mPepField;
    protected JPanel mContainer;
    protected JCheckBox mCarField;
    protected JTextField mChildCountField;
    protected JCheckBox mMarriedField;
    protected JTextField mFirstNameField;
    protected JTextField mLastNameField;
    protected JTextField mAgeField;
    protected JTextField mRegionField;
    protected JTextField mIncomeField;
    protected JComboBox mSexeField;
    protected String mSex;
    protected JButton mOkButton;
    protected JButton mResetButton;
    protected boolean mMarried;
    protected boolean mCar;
    protected boolean mSaveAct;
    protected boolean mCurrentAct;
    protected boolean mMortage;
    protected boolean mPep;
    protected OnClientFormSubmittedListener mOnClientFormSubmittedListener;

    public ClientForm () {
        mOkLabel = "Ajouter";
        mResetLabel = "Reset";
        init();
    }


    public ClientForm (Client client) {
        mOkLabel = "Sauvegarder";
        mResetLabel = "Reset";
        mIsEdit = true;
        init();
        fillWith(client);
    }


    protected void init () {
        mContainer = new JPanel();
        mContainer.setLayout(new BoxLayout(mContainer, BoxLayout.Y_AXIS));
        add(mContainer, BorderLayout.CENTER);

        mIdField = addField("ID");
        mIdField.setVisible(mIsEdit);
        mFirstNameField = addField("First name");
        mLastNameField = addField("Last name");
        mAgeField = addField("Age");
        mRegionField = addField("Region");
        mChildCountField = addField("Child count");
        mIncomeField = addField("Income");
        mSexeField = addComboField("Sex", new String[]{"Male", "Female"}, event -> mSex = (String) event.getItem());
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
        mOkButton = new JButton(mOkLabel);
        mOkButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent actionEvent) {
                addClient();
            }
        });
        mResetButton = new JButton(mResetLabel);
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
        // just for dev purpose
        fill();
        setVisible(true);
    }

    protected void fill () {
        mFirstNameField.setText("Sarah");
        mLastNameField.setText("Connors");
        mAgeField.setText("65");
        mSexeField.setSelectedIndex(1);
        mRegionField.setText("Place");
        mIncomeField.setText("35000");
        mMarriedField.setSelected(false);
        mChildCountField.setText("5");
        mCarField.setSelected(true);
        mSaveActField.setSelected(false);
        mCurrentActField.setSelected(false);
        mMortageField.setSelected(false);
        mPepField.setSelected(true);
    }

    protected void fillWith (Client client) {
        mIdField.setText(String.valueOf(client.getId()));
        mFirstNameField.setText(client.getFirstName());
        mLastNameField.setText(client.getLastName());
        mAgeField.setText(String.valueOf(client.getAge()));
        // TODO: mSexeField.setSelectedIndex(1);
        mRegionField.setText(client.getRegion());
        mIncomeField.setText(String.valueOf(client.getIncome()));
        mMarriedField.setSelected(client.isMarried());
        mChildCountField.setText(String.valueOf(client.getChildCount()));
        mCarField.setSelected(client.hasCar());
        mSaveActField.setSelected(client.hasSaveAct());
        mCurrentActField.setSelected(client.hasCurrentAct());
        mMortageField.setSelected(client.hasMortgage());
        mPepField.setSelected(client.hasPep());
    }

    protected void resetFields () {
        mIdField.setText("");
        mFirstNameField.setText("");
        mLastNameField.setText("");
        mAgeField.setText("");
        mSexeField.setSelectedIndex(0);
        mRegionField.setText("");
        mIncomeField.setText("");
        mMarriedField.setSelected(false);
        mChildCountField.setText("");
        mCarField.setSelected(false);
        mSaveActField.setSelected(false);
        mCurrentActField.setSelected(false);
        mMortageField.setSelected(false);
        mPepField.setSelected(false);
    }

    protected void addClient () {
        Client client = buildClient();
        if (mOnClientFormSubmittedListener != null) {
            mOnClientFormSubmittedListener.onSubmit(client);
        }
    }

    protected Client buildClient () {
        return new Client()
                .setId(App.parseInt(mIdField.getText()))
                .setAge(App.parseInt(mAgeField.getText()))
                .setSex((String) mSexeField.getSelectedItem())
                .setRegion(mRegionField.getText())
                .setIncome(Long.parseLong(mIncomeField.getText()))
                .setIsMarried(mMarriedField.isSelected())
                .setChildCount(App.parseInt(mChildCountField.getText()))
                .setHasCar(mCarField.isSelected())
                .setHasSaveAct(mSaveActField.isSelected())
                .setHasCurrentAct(mCurrentActField.isSelected())
                .setHasMortgage(mMortageField.isSelected())
                .setHasPep(mPepField.isSelected())
                .setFirstName(mFirstNameField.getText())
                .setLastName(mLastNameField.getText());
    }

    protected JTextField addField (String name) {
        JPanel container = new JPanel(new GridLayout(1, 2));
        JLabel label = new JLabel(name, JLabel.LEFT);
        JTextField field = new JTextField(20);
        container.add(label);
        container.add(field);
        mContainer.add(container);
        return field;
    }

    protected JCheckBox addFieldBoolean (String name, ItemListener listener) {
        JCheckBox field = new JCheckBox(name);
        field.addItemListener(listener);
        // add(field);
        return field;
    }

    protected JComboBox addComboField (String name, String[] values, ItemListener listener) {
        JPanel container = new JPanel(new GridLayout(1, 2));
        JLabel label = new JLabel(name);
        JComboBox field = new JComboBox(values);
        field.setEditable(false);
        field.addItemListener(listener);
        container.add(label);
        container.add(field);
        mContainer.add(container);
        return field;
    }

    public void setOnClientSubmittedListener (OnClientFormSubmittedListener mOnClientFormSubmittedListener) {
        this.mOnClientFormSubmittedListener = mOnClientFormSubmittedListener;
    }

    public interface OnClientFormSubmittedListener {
        void onSubmit (Client client);
    }
}