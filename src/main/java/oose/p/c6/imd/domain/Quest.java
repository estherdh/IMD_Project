package oose.p.c6.imd.domain;

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
			return reward;
		} else {
			return 0;
		}
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
