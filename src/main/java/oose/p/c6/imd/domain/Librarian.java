package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IUserDao;
import oose.p.c6.imd.service.TokenManager;

import javax.inject.Inject;

public class Librarian {
    @Inject
    private IUserDao userDao;

    public boolean verifyLogin(String email, String password) {
        User u = getUserByEmail(email);
        return u.passwordCorrect(password);
    }

    public void scanQrCode(User user, String qrCode) {
        Action qrScanAction = new QrScanAction(qrCode);
         if (user.checkQuestCompleted(qrScanAction)) {
            userDao.update(user);
        }
    }

    public User getUserByEmail(String email){
        return userDao.findUserByemail(email);
    }

	public boolean removeQuestFromQuestLog(int entryId, User user) {
		return user.removeQuestFromQuestLog(entryId);
	}
}
