package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IQuestDAO;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Inject;
import java.util.List;

public class QuestLog {
	@Inject
	IQuestDAO dao;

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

	public boolean removeQuestFromQuestLog(int entryId, int userId) {
		return dao.removeQuestFromQuestLog(entryId, userId);
	}
}
