package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EraViewQuestGenerator extends IQuestGenerator {

    public void generateQuest(int userId) {
        Map<String, String> properties = new HashMap<>();
        questTypeId = 4;

        List<Era> eras = findErasNotYetInQuestlog(userId);

        if(eras.size() > 0) {
            Era e = eras.get(new Random().nextInt(eras.size()));

            String key = "Era";
            int value = e.getId();

            properties.put(key, String.valueOf(value));

            addQuestToQuestlog(properties, userId);
        }
    }

    private List<Era> findErasNotYetInQuestlog(int userId) {
        IExhibitDao exhibitDao = DAOFactory.getExhibitDao();
        return exhibitDao.findErasNotYetInQuestlog(userId);
    }

    private void addQuestToQuestlog(Map<String, String> properties, int userId) {
        IQuestDAO questDAO = DAOFactory.getQuestDao();
        questDAO.addQuestToQuestlog(properties, userId, questTypeId);
    }
}
