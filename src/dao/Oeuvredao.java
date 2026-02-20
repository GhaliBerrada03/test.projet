package dao;

import com.mysql.cj.protocol.Resultset;
import model.Oeuvre;

import java.awt.dnd.DnDConstants;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class Oeuvredao implements daoInterface<Oeuvre> {
    @Override
    public Oeuvre findById(int idOeuvre) throws Exception {
        String sql = "SELECT * FROM oeuvre WHERE idOeuvre = ?";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idOeuvre);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    return new Oeuvre(
                            rs.getString("titre"),
                            rs.getString("artiste"),
                            rs.getString("categorie"),
                            rs.getDouble("prix"),
                            rs.getInt("idOeuvre"),
                            rs.getString("image_path"));
                }
                return null;
            }
        }
    }

    @Override
    public List<Oeuvre> findAll() throws Exception {
        // list the list of clients we have.
        List<Oeuvre> oeuvres = new ArrayList<>();
        String sql = "SELECT * FROM oeuvre";
        try (Statement st = Db_connection.getInstance().getConnection().createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                oeuvres.add(
                        new Oeuvre(
                                rs.getString("titre"),
                                rs.getString("artiste"),
                                rs.getString("categorie"),
                                rs.getDouble("prix"),
                                rs.getInt("idOeuvre"),
                                rs.getString("image_path")));

            }
            return oeuvres;
        }
    }

    @Override
    public int insert(Oeuvre oeuvre) throws Exception {
        String sql = "INSERT INTO oeuvre(titre,artiste,categorie,prix,image_path) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, oeuvre.getTitre());
            ps.setString(2, oeuvre.getArtiste());
            ps.setString(3, oeuvre.getCategorie());
            ps.setDouble(4, oeuvre.getPrix());
            ps.setString(5, oeuvre.getImagePath());
            int rows = ps.executeUpdate();
            if (rows == 1) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        oeuvre.setIdOeuvre(id);
                        return id;
                    }
                }
            }

        }
        return -1; // ntg works .
    }

    @Override
    public boolean update(Oeuvre oeuvre) throws Exception {
        String sql = "UPDATE oeuvre SET titre =? ,artiste=? , categorie=?, prix =?, image_path=? WHERE idOeuvre = ?";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, oeuvre.getTitre());
            ps.setString(2, oeuvre.getArtiste());
            ps.setString(3, oeuvre.getCategorie());
            ps.setDouble(4, oeuvre.getPrix());
            ps.setString(5, oeuvre.getImagePath());
            ps.setInt(6, oeuvre.getIdOeuvre());
            return ps.executeUpdate() == 1;
        }

    }

    @Override
    public boolean delete(int idOeuvre) throws Exception {
        String sql = "DELETE FROM oeuvre WHERE idOeuvre = ?";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, idOeuvre);
            return ps.executeUpdate() == 1;
        }
    }

    public List<Oeuvre> filtreByArtist(String artiste) throws Exception {
        List<Oeuvre> ouvres = new ArrayList<>();
        String sql = "SELECT * FROM oeuvre WHERE artiste =?";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, artiste);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ouvres.add(new Oeuvre(
                            rs.getString("titre"),
                            rs.getString("artiste"),
                            rs.getString("categorie"),
                            rs.getDouble("prix"),
                            rs.getInt("idOeuvre"),
                            rs.getString("image_path")));

                }
                return ouvres;
            }

        }

    }

    public List<Oeuvre> filtreByCategory(String categorie) throws Exception {
        List<Oeuvre> oeuvres = new ArrayList<>();
        String sql = "SELECT * FROM oeuvre WHERE categorie = ?";
        try (PreparedStatement ps = Db_connection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, categorie);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    oeuvres.add(
                            new Oeuvre(
                                    rs.getString("titre"),
                                    rs.getString("artiste"),
                                    rs.getString("categorie"),
                                    rs.getDouble("prix"),
                                    rs.getInt("idOeuvre"),
                                    rs.getString("image_path")));
                }
                return oeuvres;
            }
        }
    }
}
