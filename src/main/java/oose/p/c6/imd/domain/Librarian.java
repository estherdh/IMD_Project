package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;
import oose.p.c6.imd.persistent.dao.IUserDao;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

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

    @Inject
    private QuestGenerator questGenerator;

    public int verifyLogin(String email, String password) {
        User u = getUserByEmail(email);
        if (u == null) {
            return 1;
        }
        if (u.passwordCorrect(password)) {
            return 0;
        } else {
            return 2;
        }
    }

    public void scanQrCode(User user, String qrCode) {
        Action qrScanAction = new QrScanAction(qrCode);
        checkQuestCompleted(qrScanAction, user);
    }

    private void checkQuestCompleted(Action action, User user) {
		if (user.checkQuestCompleted(action)) {
			userDao.update(user);
		}
	}

    public User getUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    public boolean removeQuestFromQuestLog(int entryId, User user) {
        boolean success = user.removeQuestFromQuestLog(entryId);
        if (success) {
            questGenerator.generateQuest(user.getId());
            Map<String, String> notificationVariables = new HashMap<>();
            notificationVariables.put("QuestId", Integer.toString(entryId));
            sendNotification(user, 2,notificationVariables);
        }
        return success;
    }

    private void sendNotification(User user, int typeId, Map<String, String> variables) {
        user.addNotification(typeId, variables);
    }

    public boolean buyReplica(User user, int replicaId) {
        return shop.buyReplica(user, replicaId);
    }

    public List<Replica> getAvailableReplicas(User user) {
        return shop.getAvailableReplicas(user);
    }

    public Exhibit getExhibitDetails(User user, int exhibitId){
		ExhibitViewAction action = new ExhibitViewAction(exhibitId);
		checkQuestCompleted(action, user);
    	return exhibits.find(user, exhibitId);
    }

    public List<Exhibit> getAvailableExhibitsFromEra(User user, int eraId){
    	EraViewAction action = new EraViewAction(eraId);
    	checkQuestCompleted(action, user);
        return exhibits.listByEra(user, eraId);
    }
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

    public void removeUser(User user) {
        userDao.remove(user);
    }

    public int updateUser(String email, String displayName, String password, int languageId,User user) {
        return user.updateUser(email, displayName, password, languageId, user);
    }

    public int placeReplica(int replicaId, int positionId, User user) {
        return library.tryPlaceReplica(user, replicaId, positionId);
    }

    public List<Quest> getQuestLog(User user) {
        return questDAO.getQuestsForUser(user.getId(), user.getLanguageId());
    }

    public void markNotification(User user, int notificationId, boolean read){
        user.markNotification(notificationId, read);
    }

    public void addNotificationToEveryUser(Map<String, String> variables, int typeId) {
        List<User> allUsers = userDao.list();
        for (User user:allUsers) {
            user.addNotification(typeId, variables);
        }
    }

    public List<Replica> getReplicasFromUser(User user) {
        return user.getReplicas();
    }

    public int registerUser(String email, String password, String name, int languageId) {
        User newUser = new User(email, password, name, languageId);
        int validationState = 4;
        if (isNull(getUserByEmail(email))) {
            validationState = newUser.areValidCredentials();
            if (validationState == 0) {
                userDao.add(newUser);
                for (int i = 0; i < 3; i++) {
                    questGenerator.generateQuest(getUserByEmail(email).getId());
                }
            }
        }
        return validationState;
    }

    public int getMuseumByQr(String qrCode) {
        return exhibits.findMuseumByQr(qrCode);
    }
}