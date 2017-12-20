package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Quest;
import oose.p.c6.imd.domain.User;

import java.util.List;
import java.util.Map;

public interface IQuestDAO extends IDao<Quest> {
	List<Quest> getQuestsForUser(int id, int languageID);
	boolean removeQuestFromQuestLog(int entryId, int userId);
	void setQuestComplete(int entryId);
	void addQuestToQuestlog(Map<String, String> properties, int userId, int questTypeId);
	Quest find(int id, User user);
}
