package dao;

import model.Utilisateur;
import java.sql.*;

public class Utilisateurdao {
    private Connection connection;

    public Utilisateurdao() {
        try {
            connection = Db_connection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Utilisateur authenticate(String username, String password) {
        String query = "SELECT * FROM utilisateur WHERE username = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Utilisateur(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
