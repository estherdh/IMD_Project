package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IQuestDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class ViewQuestGenerator<T> extends IQuestGenerator{

    public void generateQuest(int userId) {
        Map<String, String> properties = new HashMap<>();

        List<T> values = findValuesNotYetInQuestlog(userId);

        if (values.size() > 0) {
            T obj = values.get(new Random().nextInt(values.size()));

            addQuestToQuestlog(properties, userId);
        }
    }

    void addQuestToQuestlog(Map<String, String> properties, int userId) {
        IQuestDAO questDAO = DAOFactory.getQuestDao();
        questDAO.addQuestToQuestlog(properties, userId, questTypeId);
    }

    protected abstract List<T> findValuesNotYetInQuestlog(int userId);

}
