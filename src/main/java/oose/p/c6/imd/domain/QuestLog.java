package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IQuestDAO;

import javax.inject.Inject;
import java.util.List;


public class QuestLog {

	private IQuestDAO dao;

	public QuestLog(){
        dao = DAOFactory.getQuestDao();
	}


	public int checkQuestComplete(Action action, int userId, int languageId) {
		List<Quest> questList = dao.getQuestsForUser(userId, languageId);
		int reward = 0;
        System.out.println(questList.size());
        for (Quest quest: questList) {
			int questReward = quest.checkQuestComplete(action);
			if (questReward > 0) {
				//TODO Set quest to complete.
				dao.setQuestComplete(quest.getEntryId());
				reward += questReward;
			}
		}
		return reward;
	}

	public boolean removeQuestFromQuestLog(int entryId, int userId) {
		System.out.println("entryId = [" + entryId + "], userId = [" + userId + "]");
		System.out.println("dao = " + dao);
		return dao.removeQuestFromQuestLog(entryId, userId);
	}
}
