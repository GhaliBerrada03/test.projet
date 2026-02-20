package dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import model.Client;

public class Clientdao implements daoInterface<Client> {
    @Override
    // find a client by its id that's why it ruturned an object that is Client.
    public Client findById(int idClient) throws Exception {
        String sql = " SELECT * FROM client WHERE idClient = ?";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("idClient"),
                            rs.getString("nom"),
                            rs.getString("email"));
                }
                return null;
            }
        }
    }

    @Override
    public List<Client> findAll() throws Exception {
        List<Client> Clientslist = new ArrayList<>();
        String sql = " SELECT * FROM client";
        try (Statement stt = Db_connection.getInstance().getConnection().createStatement();
                ResultSet rs = stt.executeQuery(sql)) {
            while (rs.next()) {
                Clientslist.add(
                        new Client(
                                rs.getInt("idClient"),
                                rs.getString("nom"),
                                rs.getString("email")));
            }
            return Clientslist;
        }
    }

    @Override
    public int insert(Client client) throws Exception {
        String sql = "INSERT INTO client(nom,email) VALUES (?,?)";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getEmail());
            int rows = ps.executeUpdate();
            if (rows == 1) {
                try (ResultSet key = ps.getGeneratedKeys()) {
                    if (key.next()) {
                        int id = key.getInt(1);
                        client.setId_client(id);
                        return id;
                    }

                }
            }
        }
        return -1;
    }

    @Override
    public boolean update(Client client) throws Exception {
        String sql = "UPDATE client SET nom=?, email=? WHERE idClient =?";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getEmail());
            ps.setInt(3, client.getId_client());
            return ps.executeUpdate() == 1;
        }

    }

    @Override
    public boolean delete(int idClient) throws Exception {
        String sql = "DELETE FROM client WHERE idClient = ?";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idClient);
            return ps.executeUpdate() == 1;
        }
    }
}
