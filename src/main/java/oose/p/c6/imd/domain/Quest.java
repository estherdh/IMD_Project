package oose.p.c6.imd.domain;

public class Quest {
	String name;
	String description;
	int reward;
	IQuestType questStrategy;

	public Quest(String name, String description, int reward, IQuestType questStrategy) {
		this.name = name;
		this.description = description;
		this.reward = reward;
		this.questStrategy = questStrategy;
	}

	public int checkQuestComplete(Action action) {
		if (questStrategy.checkQuestComplete(action)) {
			return reward;
		} else {
			return 0;
		}
	}
}
