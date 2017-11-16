package oose.p.c6.imd.Service;

import oose.p.c6.imd.Dao.QuestDao;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class User {
	@Inject
	private QuestDao questDao;

	private int userId;

	public User() {
		TokenManager.addUser(this);
	}

	public Response removeQuest(int entryId) {
		if (questDao.removeQuest(entryId, userId)) {
			return Response.status(200).build();
		} else {
			return Response.status(400).build();
		}
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
