package services;

import dao.Utilisateurdao;
import model.Utilisateur;

public class AuthService {
    private Utilisateurdao userdao = new Utilisateurdao();

    public Utilisateur login(String username, String password) {
        return userdao.authenticate(username, password);
    }
}
