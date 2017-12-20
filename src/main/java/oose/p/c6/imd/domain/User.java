package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IUserDao;

import oose.p.c6.imd.persistent.dao.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User extends Model {
    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    private IUserDao userDao = DAOFactory.getUserDao();
    private IReplicaDao replicaDao = DAOFactory.getReplicaDao();

    private String email;
    private String password;
    private String displayName;
    private int coins;
    private int languageId;
    private QuestLog questLog;

    public User() {
    }

    public User(int id, String email, String password, String displayName, int coins, int languageId) {
        super(id);
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.coins = coins;
        this.languageId = languageId;
        this.questLog = new QuestLog();
    }

    public boolean passwordCorrect(String actual) {
        return hashPassword(actual).equals(password);
    }

    public String hashPassword(String toHash) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(toHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return generatedPassword;
    }

    public void removeCoins(int coins) {
        this.coins -= coins;
        userDao.update(this);
    }

    public void addReplicaToInventory(Replica replica) {
        replicaDao.giveReplicaToUser(this, replica);
    }

    public boolean checkQuestCompleted(Action action) {
        int newCoins = questLog.checkQuestComplete(action, super.getId(), languageId);
        if (newCoins > 0) {
            coins += newCoins;
            return true;
        }
        return false;
    }

    public boolean userHasReplica(Replica replica) {
        List<Replica> replicaList = replicaDao.getReplicasFromUser(this);
        for (Replica r : replicaList) {
            if (replica.getId() == r.getId()) {
                return true;
            }
        }
        return false;
    }

    public int updateUser(String email, String name, String password, int languageId, User user) {
        if (isValidEmailAddress(email)){
            if (isValidDisplayName(name)) {
                if (isValidPassword(password)) {
                    user.setEmail(email);
                    user.setDisplayName(name);
                    user.setPassword(user.hashPassword(password));
                    user.setLanguageId(languageId);
                    userDao.update(user);
                    // success
                    return 0;
                }
                //invalid password
                return 1;
            }
            //invalid displayname
            return 2;
        }
        //invalid emailadress
        return 3;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,100}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches() && email.length() <= 100;
    }

    public boolean isValidDisplayName(String name) {
        if (!name.trim().isEmpty()) {
            String ePattern = "^[-a-zA-Z0-9_ ]{2,50}+$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(name);
            return m.matches();
        }
        return false;
    }

    public boolean isValidPassword(String password) {
        return !password.trim().isEmpty() && password.length() <= 128 && password.length() >= 6;
    }

    public void addNotification(int typeId, Map<String, String> properties) {
        userDao.addNotification(typeId, properties, this);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean removeQuestFromQuestLog(int entryId) {
        return questLog.removeQuestFromQuestLog(entryId, super.getId());
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public void setQuestLog(QuestLog questLog) {
        this.questLog = questLog;
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    public void setReplicaDao(IReplicaDao replicaDao) {
        this.replicaDao = replicaDao;
    }

    public List<Replica> getReplicas() {
        return replicaDao.getReplicasFromUser(this);
    }
}
