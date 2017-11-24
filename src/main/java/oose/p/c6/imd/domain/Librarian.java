package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IUserDao;

import javax.inject.Inject;
import java.util.List;

public class Librarian {
    @Inject
    private IUserDao users;

    @Inject
    private Shop shop;

    public boolean verifyLogin(String email, String password) {
        User u = getUserByEmail(email);
        return u.passwordCorrect(password);
    }

    public User getUserByEmail(String email) {
        return users.findUserByemail(email);
    }

    public boolean buyReplica(User user, int replicaId) {
        return shop.buyReplica(user, replicaId);
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
