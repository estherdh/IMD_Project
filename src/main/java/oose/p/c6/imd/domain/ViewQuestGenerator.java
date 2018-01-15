package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;
import oose.p.c6.imd.persistent.dao.IUserDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ViewQuestGenerator extends BaseQuestGenerator {

    Map<String, String> properties = new HashMap<>();
    List<String> valuesById = new ArrayList<>();

    IExhibitDao exhibitDao = DAOFactory.getExhibitDao();
    IUserDao userDao = DAOFactory.getUserDao();

    public abstract void generateQuest(int userId);

    void setProperties(String key, String value) {
        properties.put(key, value);
    }

    void addQuestToQuestlog(int userId) {
        IQuestDAO questDAO = DAOFactory.getQuestDao();
        questDAO.addQuestToQuestlog(properties, userId, questTypeId, valuesById);
    }
}
