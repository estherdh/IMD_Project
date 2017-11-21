package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IQuest;

import javax.inject.Inject;
import java.util.List;

public class QuestLog {
	@Inject
	IQuest dao;

	public int checkQuestComplete(Action action, int userId, int languageId) {
		List<Quest> questList = dao.getQuestsForUser(userId, languageId);
		int reward = 0;
		for (Quest quest: questList) {
			int questReward = quest.checkQuestComplete(action);
			if (questReward > 0) {
				//TODO Set quest to complete.
				//dao.completequest(quest);
				reward += questReward;
			}
		}
		return reward;
	}
}
