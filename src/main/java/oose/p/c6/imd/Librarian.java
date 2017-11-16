package oose.p.c6.imd;

import oose.p.c6.imd.Service.User;
import oose.p.c6.imd.Service.TokenManager;

import javax.ws.rs.core.Response;

public class Librarian {

	//TODO: Aanroepen van buitenaf door REST.
	public Response removeQuest(int entryId, String token) {
		User user = TokenManager.getUserByToken(token);
		return user.removeQuest(entryId);
	}
}
