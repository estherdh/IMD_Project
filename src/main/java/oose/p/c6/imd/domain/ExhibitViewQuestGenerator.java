package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExhibitViewQuestGenerator extends IQuestGenerator {

    @Override
    public void generateQuest(int userId) {
        HashMap<String, String> properties = new HashMap<>();
        questTypeId = 3;

        List<Exhibit> exhibits = findExhibitsNotYetInQuestlog(userId);

        if (!exhibits.isEmpty()) {
            Exhibit e = exhibits.get(new Random().nextInt(exhibits.size()));

            String key = "Topstuk";
            int value = e.getId();
            properties.put(key, value + "");

            addQuestToQuestlog(properties, userId);
        }
    }

    private List<Exhibit> findExhibitsNotYetInQuestlog(int userId) {
        IExhibitDao exhibitDao = DAOFactory.getExhibitDao();
        return exhibitDao.findExhibitsNotYetInQuestlog(userId);
    }

    private void addQuestToQuestlog(Map<String, String> properties, int userId) {
        IQuestDAO questDAO = DAOFactory.getQuestDao();
        questDAO.addQuestToQuestlog(properties, userId, questTypeId);
    }
}
