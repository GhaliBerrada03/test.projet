package services;

import dao.Oeuvredao;
import model.Oeuvre;
import java.util.List;

public class OeuvreServices {
    private Oeuvredao oeuvreDao = new Oeuvredao();

    public List<Oeuvre> getAllOeuvres() throws Exception {
        return oeuvreDao.findAll();
    }

    public void addOeuvre(Oeuvre oeuvre) throws Exception {
        if (oeuvre.getPrix() < 0)
            throw new Exception("Price cannot be negative");
        oeuvreDao.insert(oeuvre);
    }

    public void updateOeuvre(Oeuvre oeuvre) throws Exception {
        oeuvreDao.update(oeuvre);
    }

    public void deleteOeuvre(int id) throws Exception {
        oeuvreDao.delete(id);
    }

    public List<Oeuvre> searchByArtist(String artist) throws Exception {
        return oeuvreDao.filtreByArtist(artist);
    }
}
