package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IUserDao;
import oose.p.c6.imd.service.TokenManager;

import javax.inject.Inject;

public class Librarian {
    @Inject
    private IUserDao userDao;

    public int verifyLogin(String email, String password) {
        User u = getUserByEmail(email);
        if(u == null) {
            return 1;
        }
        if(u.passwordCorrect(password)){
            return 0;
        } else {
            return 2;
        }
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
        System.out.println("entryId = [" + entryId + "], user = [" + user + "]");
        return user.removeQuestFromQuestLog(entryId);
	}
}
