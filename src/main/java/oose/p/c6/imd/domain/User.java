package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User extends Model {

    private IUserDao userDao = DAOFactory.getUserDao();

    private IReplicaDao replicaDao = DAOFactory.getReplicaDao();
    //private QuestLog ql;
	private String email;
    private String password;
    private String display_name;
    private int coins;
    private int languageId;

    public User() { }

	public User(int id, String email, String password, String display_name, int coins, int languageId){
		super(id);
		this.email = email;
		this.password = password;
		this.display_name = display_name;
		this.coins = coins;
		this.languageId = languageId;
	}

	public boolean passwordCorrect(String actual){
        return hashPassword(actual).equals(password);
    }

    public String hashPassword(String toHash){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(toHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public void removeCoins(int coins) {
        this.coins -= coins;
        System.out.println(this.coins);
        userDao.update(this);
    }

    public void addReplicaToInventory(Replica replica) {
        replicaDao.giveReplicaToUser(this, replica);
    }

    //TODO revamp method
//	public Response removeQuest(int entryId) {
//		if (questDao.removeQuest(entryId, id)) {
//			return Response.status(200).build();
//		} else {
//			return Response.status(400).build();
//		}
//	}

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

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
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
}
