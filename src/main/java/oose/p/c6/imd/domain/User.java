package oose.p.c6.imd.domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User extends Model {
	//private QuestLog ql;
	private String email;
    private String password;
    private String display_name;
    private int coins;
    private int taalId;
    private QuestLog questLog;

	public User(int id, String email, String password, String display_name, int coins, int taalId){
		super(id);
		this.email = email;
		this.password = password;
		this.display_name = display_name;
		this.coins = coins;
		this.taalId = taalId;
		this.questLog = new QuestLog();
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

    public void checkQuestCompleted(Action action) {
		int newCoins = questLog.checkQuestComplete(action);
		if (newCoins > 0) {
			coins += newCoins;
			//TODO Updaten user naar de database. Update functie bestaat nog niet
		}
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

	public String getPassword() {
		return password;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public int getCoins() {
		return coins;
	}

	//test functie.
	public void setQuestLog(QuestLog questLog) {
    	this.questLog = questLog;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}
}
