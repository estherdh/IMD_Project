package oose.p.c6.imd.domain;

import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.service.TokenManager;

import javax.ws.rs.core.Response;

public class Librarian {
    public static void main(String args[]){
        System.out.println(new User(1, "a", "a", "a", 100).hashPassword("test"));

    }

    public User getUser(String username){
        return new User(1, username, "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "a", 100);
    }
//	TODO: Aanroepen van buitenaf door REST.
//	public Response removeQuest(int entryId, String token) {
//		User user = TokenManager.getInstance().getUserFromToken(token);
//		return user.removeQuest(entryId);
//	}
}
