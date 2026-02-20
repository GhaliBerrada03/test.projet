package services;

import dao.Clientdao;
import model.Client;
import java.util.List;

public class ClientService {
    private Clientdao clientDao = new Clientdao();

    public List<Client> getAllClients() throws Exception {
        return clientDao.findAll();
    }

    public void addClient(Client client) throws Exception {
        if (!client.getEmail().contains("@"))
            throw new Exception("Invalid email format");
        clientDao.insert(client);
    }

    public void updateClient(Client client) throws Exception {
        clientDao.update(client);
    }

    public void deleteClient(int id) throws Exception {
        clientDao.delete(id);
    }
}
