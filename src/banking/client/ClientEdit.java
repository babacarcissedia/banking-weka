package banking.client;

import banking.Database;
import banking.model.Client;

import javax.swing.*;
import java.sql.*;

public class ClientEdit extends ClientForm {
    protected OnClientUpdatedListener mOnClientUpdatedListener;
    public ClientEdit (Client client) {
        super(client);
        // done in parent constructor
        // fillWith(client);
        setOnClientSubmittedListener(c -> {
            onUpdate(client.getId(), c);
        });
    }

    private void onUpdate (int oldId, Client client) {
        Connection con = Database.getAppConnection();
        if (con == null) {
            System.out.println("Impossible de se connecter à la base de données");
            return;
        }
        try {
            PreparedStatement statement = con.prepareStatement("UPDATE clients SET " +
                    "age = ?," +
                    "sex = ?," +
                    "region = ?," +
                    "income = ?," +
                    "is_married = ?," +
                    "child_count = ?," +
                    "has_car = ?," +
                    "has_save_act = ?," +
                    "has_current_act = ?," +
                    "has_mortgage = ?," +
                    "has_pep = ?," +
                    "first_name = ?," +
                    "last_name = ?," +
                    "id = ? " +
                    "WHERE id = ?"
            );
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
            statement.setInt(14, client.getId());
            statement.setInt(15, oldId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
            JOptionPane.showMessageDialog(this, "Client modifié.");
            if (mOnClientUpdatedListener != null) {
                mOnClientUpdatedListener.onClientUpdated(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public interface OnClientUpdatedListener {
        void onClientUpdated(Client client);
    }

    public void setOnClientUpdatedListener (OnClientUpdatedListener mOnClientUpdatedListener) {
        this.mOnClientUpdatedListener = mOnClientUpdatedListener;
    }
}