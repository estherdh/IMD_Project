package oose.p.c6.imd.domain;

import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.service.TokenManager;

import javax.ws.rs.core.Response;

public class Librarian {

	//TODO: Aanroepen van buitenaf door REST.
	public Response removeQuest(int entryId, String token) {
		User user = TokenManager.getInstance().getUserFromToken(token);
		return user.removeQuest(entryId);
	}
}
