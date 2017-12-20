package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IExhibitDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EraViewQuestGenerator extends ViewQuestGenerator {

    public void generateQuest(int userId) {
        Map<String, String> properties = new HashMap<>();
        questTypeId = 4;

        List<Era> eras = findValuesNotYetInQuestlog(userId);

        if(eras.size() > 0) {
            Era e = eras.get(new Random().nextInt(eras.size()));

            String key = "Era";
            int value = e.getId();

            properties.put(key, String.valueOf(value));

            addQuestToQuestlog(properties, userId);
        }
    }


    @Override
    protected List<Era> findValuesNotYetInQuestlog(int userId) {
        IExhibitDao exhibitDao = DAOFactory.getExhibitDao();
        return exhibitDao.findErasNotYetInQuestlog(userId);
    }
}
