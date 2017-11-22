package oose.p.c6.imd.domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User extends Model {
	//private QuestLog ql;
	private String email;
    private String password;
    private String display_name;
    private int coins;
    private int languageId;
    private QuestLog questLog;

	public User(int id, String email, String password, String display_name, int coins, int languageId){
		super(id);
		this.email = email;
		this.password = password;
		this.display_name = display_name;
		this.coins = coins;
		this.languageId = languageId;
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
		int newCoins = questLog.checkQuestComplete(action, super.getId(), languageId);
		if (newCoins > 0) {
			coins += newCoins;
			//TODO Updaten user naar de database. Update functie bestaat nog niet
		}
	}

	public boolean removeQuestFromQuestLog(int entryId) {
		return questLog.removeQuestFromQuestLog(entryId, super.getId());
	}

	public int getCoins() {
		return coins;
	}

	public void setQuestLog(QuestLog questLog) {
    	this.questLog = questLog;
	}
}
