package oose.p.c6.imd.domain;

import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.dao.IUserDao;
import oose.p.c6.imd.persistent.dao.UserJDBCDao;
import oose.p.c6.imd.service.RESTService;
import oose.p.c6.imd.service.TokenManager;

import javax.inject.Inject;
import javax.json.*;
import javax.ws.rs.core.Response;
import java.util.List;

public class Librarian {
    @Inject
    private IUserDao users;

    private Shop shop = new Shop();

    public boolean verifyLogin(String email, String password) {
        User u = getUserByEmail(email);
        return u.passwordCorrect(password);
    }

    public User getUserByEmail(String email) {
        return users.findUserByemail(email);
    }

    public void buyReplica(User user, int replicaId) {

    }

    public List<Replica> getAvailableReplicas(User user) {
        return shop.getAvailableReplicas(user);
    }

//	TODO: Aanroepen van buitenaf door REST.
//	public Response removeQuest(int entryId, String token) {
//		User user = TokenManager.getInstance().getUserFromToken(token);
//		return user.removeQuest(entryId);
//	}
}
