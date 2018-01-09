package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseQuestGenerator {
    int questTypeId;
    Map<String, String> properties = new HashMap<>();
    IExhibitDao exhibitDao = DAOFactory.getExhibitDao();

    public abstract void generateQuest(int userId);

    void setProperties(String key, String value) {
        properties.put(key, value);
    }

    void addQuestToQuestlog(int userId) {
        IQuestDAO questDAO = DAOFactory.getQuestDao();
        questDAO.addQuestToQuestlog(properties, userId, questTypeId);
    }
}
