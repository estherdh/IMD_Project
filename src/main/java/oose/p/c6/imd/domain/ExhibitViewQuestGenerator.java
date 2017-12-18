package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExhibitViewQuestGenerator extends IQuestGenerator {

    @Override
    public void generateQuest(int userId) {
        Map<String, String> properties = new HashMap<>();
        questTypeId = 3;

        List<Exhibit> exhibits = findExhibitsNotYetInQuestlog(userId);

        if(exhibits.size() > 0) {
            Exhibit e = exhibits.get(new Random().nextInt(exhibits.size()));

            String key = "Topstuk";
            int value = e.getId();
            properties.put(key, value + "");

            addQuestToQuestlog(properties, userId, questTypeId);
        }
    }

    List<Exhibit> findExhibitsNotYetInQuestlog(int userId) {
        IExhibitDao exhibitDao = DAOFactory.getExhibitDao();
        return   exhibitDao.findExhibitsNotYetInQuestlog(userId);
    }

    void addQuestToQuestlog(Map<String, String> properties, int userId, int questTypeId) {
        IQuestDAO questDAO = DAOFactory.getQuestDao();
        questDAO.addQuestToQuestlog(properties, userId, questTypeId);
    }


}
