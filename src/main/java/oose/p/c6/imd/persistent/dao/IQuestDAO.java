package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Quest;

import java.util.List;

public interface IQuestDAO extends IDao<Quest> {
	List<Quest> getQuestsForUser(int id, int languageID);
}
