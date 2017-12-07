package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;

import java.util.HashMap;
import java.util.Random;

public class ExhibitViewQuestGenerator extends IQuestGenerator {

    private IQuestDAO questDAO;
    private IExhibitDao exhibitDao;

    @Override
    public void generateQuest(int userId) {
        HashMap<String, String> properties = new HashMap<>();
        questTypeId = 1;

        Random r = new Random(exhibitDao.findExhibitsNotYetInQuestlog(userId).size());
        Exhibit e = exhibitDao.findExhibitsNotYetInQuestlog(userId).get(r.nextInt());

        int exhibitId = e.getId();
        String key = "Topstuk";
        int value = e.getId();

        properties.put("Key", key);
        properties.put("Value", String.valueOf(value));
        properties.put("ExhibitId", String.valueOf(exhibitId));

        questDAO.addQuestToQuestlog(properties, userId, questTypeId);
    }
}
