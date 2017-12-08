package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.ExhibitJDBCDao;
import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;
import oose.p.c6.imd.persistent.dao.QuestJDBCDao;

import java.util.HashMap;
import java.util.Random;

public class ExhibitViewQuestGenerator extends IQuestGenerator {

    private IQuestDAO questDAO = new QuestJDBCDao();
    private IExhibitDao exhibitDao = new ExhibitJDBCDao();

    @Override
    public void generateQuest(int userId) {
        HashMap<String, String> properties = new HashMap<>();
        questTypeId = 3;

        Random r = new Random(exhibitDao.findExhibitsNotYetInQuestlog(userId).size());
        Exhibit e = exhibitDao.findExhibitsNotYetInQuestlog(userId).get(r.nextInt());

        String key = "Topstuk";
        int value = e.getId();

        properties.put("Key", key);
        properties.put("Value", String.valueOf(value));

        questDAO.addQuestToQuestlog(properties, userId, questTypeId);
    }
}
