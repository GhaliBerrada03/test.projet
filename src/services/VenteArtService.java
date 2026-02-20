package services;

import dao.VenteArtdao;
import model.VenteArt;
import java.util.List;

public class VenteArtService {
    private VenteArtdao venteDao = new VenteArtdao();

    public List<VenteArt> getAllSales() throws Exception {
        return venteDao.findAll();
    }

    public void recordSale(VenteArt vente) throws Exception {
        venteDao.insert(vente);
    }

    public void deleteSale(int id) throws Exception {
        venteDao.delete(id);
    }
}
