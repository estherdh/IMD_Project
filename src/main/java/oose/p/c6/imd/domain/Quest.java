package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IUserDao;

import java.util.HashMap;
import java.util.Map;

public class Quest {
	int entryId;
	String name;
	String description;
	int reward;
	IQuestType questType;

	public Quest(int entryId, String name, String description, int reward, IQuestType questType) {
		this.entryId = entryId;
		this.name = name;
		this.description = description;
		this.reward = reward;
		this.questType = questType;
	}

	public int checkQuestComplete(Action action) {
		if (questType.checkQuestComplete(action)) {
			sendNotification();
			return reward;
		} else {
			return 0;
		}
	}

	private void sendNotification() {
		IUserDao userDao = DAOFactory.getUserDao();
		User user = userDao.getUserByQuestId(entryId);
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("QuestId", Integer.toString(entryId));
		variables.put("Coins", Integer.toString(user.getCoins() + reward));
		userDao.addNotification(1, variables, user);
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getReward() {
		return reward;
	}

	public IQuestType getQuestType() {
		return questType;
	}

	public void setQuestType(IQuestType questType) {
		this.questType = questType;
	}
}
