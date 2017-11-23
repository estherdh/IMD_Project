package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IUserDao;

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
//	TODO: Aanroepen van buitenaf door REST.
//	public Response removeQuest(int entryId, String token) {
//		User user = TokenManager.getInstance().getUserFromToken(token);
//		return user.removeQuest(entryId);
//	}
}
