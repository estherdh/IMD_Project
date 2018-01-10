package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IUserDao;

import java.util.HashMap;
import java.util.Map;

public class Quest {
	private int entryId;
	private String name;
	private String questTypeDescription;
	private int reward;
	private IQuestType questType;
	private int questTypeId;
	private int removed;
	private int completed;
	private String questDescription;
	private String valueOfQuest;

	public Quest(int entryId, String name, String questTypeDescription, int reward, int questTypeId, int removed, int completed, IQuestType questType) {
		this.entryId = entryId;
		this.name = name;
		this.questTypeDescription = questTypeDescription;
		this.reward = reward;
		this.questTypeId = questTypeId;
		this.removed = removed;
		this.completed = completed;
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

	public void setQuestDescription(String description) {
		this.questDescription = description;
	}

	public void setValueOfQuest(String value) {
		this.valueOfQuest = value;
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

	public void setQuestTypeDescription(String questTypeDescription) {
		this.questTypeDescription = questTypeDescription;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public String getName() {
		return name;
	}

	public String getQuestTypeDescription() {
		return questTypeDescription;
	}

	public int getReward() {
		return reward;
	}

	public IQuestType getQuestType() {
		return questType;
	}

	public int getQuestTypeId() { return questTypeId; }

	public int getCompleted() { return completed; }

	public int getRemoved() { return removed; }

	public void setQuestType(IQuestType questType) {
		this.questType = questType;
	}

	public String getQuestDescription() {
		return questDescription;
	}
}
