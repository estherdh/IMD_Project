package oose.p.c6.imd.domain;

import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.dao.IUserDao;
import oose.p.c6.imd.persistent.dao.UserJDBCDao;
import oose.p.c6.imd.service.RESTService;
import oose.p.c6.imd.service.TokenManager;

import javax.inject.Inject;
import javax.json.*;
import javax.ws.rs.core.Response;

public class Librarian {
    @Inject
    private IUserDao users;

    public String tested(){
        return users.helloWorld();
    }

    public boolean verifyLogin(String email, String password) {
        User u = getUserByEmail(email);
        return u.passwordCorrect(password);
    }

    public void scanQrCode(User user, String qrCode) {
        Action qrScanAction = new QrScanAction(qrCode);
        user.
    }

    public User getUserByEmail(String email){
        return users.findUserByemail(email);
    }
//	TODO: Aanroepen van buitenaf door REST.
//	public Response removeQuest(int entryId, String token) {
//		User user = TokenManager.getInstance().getUserFromToken(token);
//		return user.removeQuest(entryId);
//	}
}
