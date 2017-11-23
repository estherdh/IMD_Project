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

    public boolean verifyLogin(String email, String password) {
        User u = getUserByEmail(email);
        return u.passwordCorrect(password);
    }

    public void scanQrCode(User user, String qrCode) {
        Action qrScanAction = new QrScanAction(qrCode);
        user.checkQuestCompleted(qrScanAction);
    }

    public User getUserByEmail(String email){
        return users.findUserByemail(email);
    }

	public boolean removeQuestFromQuestLog(int entryId, String token) {
		User user = TokenManager.getInstance().getUserFromToken(token);
		return user.removeQuestFromQuestLog(entryId);
	}
}
