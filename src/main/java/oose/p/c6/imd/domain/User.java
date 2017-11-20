package oose.p.c6.imd.domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User extends Model {
	//private QuestLog ql;
	private String email;
    private String password;
    private String display_name;
    private int coins;

	public User(int id, String email, String password, String display_name, int coins){
		super(id);
		this.email = email;
		this.password = password;
		this.display_name = display_name;
		this.coins = coins;
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

    //TODO revamp method
//	public Response removeQuest(int entryId) {
//		if (questDao.removeQuest(entryId, id)) {
//			return Response.status(200).build();
//		} else {
//			return Response.status(400).build();
//		}
//	}
}
