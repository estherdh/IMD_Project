package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;
import oose.p.c6.imd.persistent.dao.IUserDao;

import javax.inject.Inject;
import java.util.List;

public class Librarian {
    @Inject
    private IUserDao userDao;

    @Inject
    private IQuestDAO questDAO;

    @Inject
    private Shop shop;

    @Inject
    private Library library;

    @Inject
    private IExhibitDao exhibits;

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
        return user.removeQuestFromQuestLog(entryId);
	}

    public boolean buyReplica(User user, int replicaId) {
        return shop.buyReplica(user, replicaId);
    }

    public List<Replica> getAvailableReplicas(User user) {
        return shop.getAvailableReplicas(user);
    }

    public Exhibit getExhibitDetails(User user, int exhibitId){return exhibits.find(user, exhibitId);}

    public List<Exhibit> getAvailableExhibitsFromEra(User user, int eraId){return exhibits.listByEra(user, eraId);}
    public List<Exhibit> getAvailableExhibitsFromMuseum(User user, int museumId){return exhibits.listByMuseum(user, museumId);}
    public List<Exhibit> getAvailableExhibits(User user){return exhibits.list(user);}

    public Era findEra(User user, int eraId){
       return exhibits.findEra(user, eraId);
    }
    public List<Era> listEra(User user){
        return exhibits.listEra(user);
    }
    public Museum findMuseum(int museumId){
        return exhibits.findMuseum(museumId);
    }
    public List<Museum> listMuseums(){
        return exhibits.listMuseums();
    }




    public int placeReplica(int replicaId, int positionId, User user) {
        return library.tryPlaceReplica(user, replicaId, positionId);
    }

    public List<Quest> getQuestLog(User user) {
        return questDAO.getQuestsForUser(user.getId(), user.getLanguageId());
    }

    public List<Replica> getReplicasFromUser(User user) {
        return user.getReplicas();
    }
}
