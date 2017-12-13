package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.*;

import java.util.HashMap;
import java.util.Random;

public class ExhibitViewQuestGenerator extends IQuestGenerator {

    private IQuestDAO questDAO = DAOFactory.getQuestDao();
    private IExhibitDao exhibitDao = DAOFactory.getExhibitDao();

    protected ExhibitViewQuestGenerator(int questTypeId) {
        super(questTypeId);
    }

    @Override
    public void generateQuest(int userId) {
        HashMap<String, String> properties = new HashMap<>();

        Random r = new Random(exhibitDao.findExhibitsNotYetInQuestlog(userId).size());
        Exhibit e = exhibitDao.findExhibitsNotYetInQuestlog(userId).get(r.nextInt());

        String key = "Topstuk";
        int value = e.getId();

        properties.put("Key", key);
        properties.put("Value", String.valueOf(value));

        questDAO.addQuestToQuestlog(properties, userId, questTypeId);
    }
}
