package banking.client;

import banking.model.Client;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ClientTableModel extends DefaultTableModel {
    private Class[] mColumnTypes = new Class[]{Long.class, String.class, String.class, Long.class, String.class, Integer.class, Long.class, String.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class, Boolean.class};
    private String[][] mData = new String[5][5];
    private ArrayList<Client> mClients = new ArrayList<>();
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

    public ClientTableModel (ArrayList<Client> clients) {
        mClients = new ArrayList<>(clients);
        mData = new String[clients.size()][mColumns.length];
        for (int i=0; i<clients.size(); i++) {
            for (int j=0; j<mColumns.length; j++) {
                mData[i][j] = getColumnValue(clients.get(i), j);
            }
        }
    }

    private String getColumnValue (Client client, int j) {
        switch (j) {
            case 0: return String.valueOf(client.getId());
            case 1: return client.getFirstName();
            case 2: return client.getLastName();
            case 3: return String.valueOf(client.getAge());
            case 4: return client.getRegion();
            case 5: return String.valueOf(client.getChildCount());
            case 6: return String.valueOf(client.getIncome());
            case 7: return client.getSex();
            case 8: return String.valueOf(client.isMarried());
            case 9: return String.valueOf(client.hasSaveAct());
            case 10: return String.valueOf(client.hasCurrentAct());
            case 11: return String.valueOf(client.hasMortgage());
            case 12: return String.valueOf(client.hasCar());
            case 13: return String.valueOf(client.hasPep());
        }
        return "N/A";
    }


    @Override
    public Class<?> getColumnClass (int column) {
        return mColumnTypes[column];
    }

    @Override
    public boolean isCellEditable (int i, int i1) {
        return false;
    }

    public String getColumnName (int column) {
        return mColumns[column];
    }

    public int getRowCount () { return mData != null ? mData.length : 0; }

    public int getColumnCount () { return mColumns.length; }

    public Object getValueAt (int row, int col) {
        return getColumnValue(mClients.get(row), col);
    }

    public void setValueAt (Object value, int row, int col) {
        mData[row][col] = (String) value;
        fireTableCellUpdated(row, col);
    }
}
