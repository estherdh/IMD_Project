package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;

import java.util.HashMap;
import java.util.Random;

public class EraViewQuestGenerator extends IQuestGenerator {

    private IQuestDAO questDAO;
    private IExhibitDao exhibitDao;

    public void generateQuest(int userId) {
        HashMap<String, String> properties = new HashMap<>();
        questTypeId = 4;

        Random r = new Random(chanceQuest);
        Era e = exhibitDao.findErasNotYetInQuestlog(userId).get(r.nextInt());

        String key = "Era";
        int value = e.getId();

        properties.put("Key", key);
        properties.put("Value", String.valueOf(value));

        questDAO.addQuestToQuestlog(properties, userId, questTypeId);
    }
}
