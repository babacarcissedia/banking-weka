package banking.client;

import banking.Database;
import banking.model.Client;

import javax.swing.*;
import java.sql.*;

public class ClientCreate extends ClientForm {
    private OnClientAddedListener mOnClientAddedListener;

    public ClientCreate () {
        super();
        setOnClientSubmittedListener(c -> onCreate(c));
    }

    private void onCreate (Client client) {
        Connection con = Database.getAppConnection();
        if (con == null) {
            System.out.println("Impossible de se connecter à la base de données");
            return;
        }
        try {
            PreparedStatement statement = con.prepareStatement("INSERT INTO clients " +
            "(age, sex, region, income, is_married, child_count, has_car, has_save_act, has_current_act, has_mortgage, has_pep, first_name, last_name)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            , Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, client.getAge());
            statement.setString(2, client.getSex());
            statement.setString(3, client.getRegion());
            statement.setLong(4, client.getIncome());
            statement.setBoolean(5, client.isMarried());
            statement.setInt(6, client.getChildCount());
            statement.setBoolean(7, client.hasCar());
            statement.setBoolean(8, client.hasSaveAct());
            statement.setBoolean(9, client.hasCurrentAct());
            statement.setBoolean(10, client.hasMortgage());
            statement.setBoolean(11, client.hasPep());
            statement.setString(12, client.getFirstName());
            statement.setString(13, client.getLastName());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
                client.setId(generatedKeys.getInt(1));
                if (mOnClientAddedListener != null) {
                    mOnClientAddedListener.onClientAdded(client);
                }
            }
            JOptionPane.showMessageDialog(null, "Client ajouté.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void setOnClientAddedListener (OnClientAddedListener mOnClientAddedListener) {
        this.mOnClientAddedListener = mOnClientAddedListener;
    }

    public interface OnClientAddedListener {
        void onClientAdded(Client client);
    }
}