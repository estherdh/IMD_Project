package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IExhibitDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExhibitViewQuestGenerator extends ViewQuestGenerator {

    @Override
    public void generateQuest(int userId) {
        Map<String, String> properties = new HashMap<>();
        questTypeId = 3;

        List<Exhibit> exhibits = findValuesNotYetInQuestlog(userId);

        if (exhibits.size() > 0) {
            Exhibit e = exhibits.get(new Random().nextInt(exhibits.size()));

            String key = "Topstuk";
            int value = e.getId();
            properties.put(key, value + "");

            addQuestToQuestlog(properties, userId);
        }
    }

    @Override
    protected List<Exhibit> findValuesNotYetInQuestlog(int userId) {
        IExhibitDao exhibitDao = DAOFactory.getExhibitDao();
        return exhibitDao.findExhibitsNotYetInQuestlog(userId);
    }
}
