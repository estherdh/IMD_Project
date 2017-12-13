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
        int chanceRemovedQuest = 10;
        int chanceQuest = 100;
        boolean isRemoved;

        Random r = new Random(chanceQuest);
        if(r.nextInt() <= chanceRemovedQuest) {
            isRemoved = true;
        } else {
            isRemoved = false;
        }
        r = new Random(exhibitDao.findErasNotYetInQuestlog(userId, isRemoved).size());
        Era e = exhibitDao.findErasNotYetInQuestlog(userId, isRemoved).get(r.nextInt());

        String key = "Era";
        int value = e.getId();

        properties.put("Key", key);
        properties.put("Value", String.valueOf(value));

        questDAO.addQuestToQuestlog(properties, userId, questTypeId);
    }
}
