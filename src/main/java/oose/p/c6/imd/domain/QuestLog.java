package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IQuestDAO;

import java.util.List;


public class QuestLog {

	private IQuestDAO dao;

	private QuestGenerator questGenerator;

	public QuestLog(){
        dao = DAOFactory.getQuestDao();
        questGenerator = new QuestGenerator();
	}


	public int checkQuestComplete(Action action, int userId, int languageId) {
		List<Quest> questList = dao.getQuestsForUser(userId, languageId);
		int reward = 0;
        for (Quest quest: questList) {
			int questReward = quest.checkQuestComplete(action);
			if (questReward > 0) {
				dao.setQuestComplete(quest.getEntryId());
				reward += questReward;
				questGenerator.generateQuest(userId);
			}
		}
		return reward;
	}

	public boolean removeQuestFromQuestLog(int entryId, int userId) {
		return dao.removeQuestFromQuestLog(entryId, userId);
	}

	public void setQuestGenerator(QuestGenerator questGenerator) {
		this.questGenerator = questGenerator;
	}
}
