package oose.p.c6.imd.domain;

import java.util.List;
import java.util.Random;

public class ExhibitViewQuestGenerator extends BaseQuestGenerator {

    public void generateQuest(int userId) {
        questTypeId = 3;

        List<Exhibit> exhibits = findExhibitsNotYetInQuestlog(userId);

        if (!exhibits.isEmpty()) {
            Exhibit e = exhibits.get(new Random().nextInt(exhibits.size()));

            String key = "Topstuk";
            String value = String.valueOf(e.getId());
            setProperties(key, value);

            addQuestToQuestlog(userId);
        }
    }

    private List<Exhibit> findExhibitsNotYetInQuestlog(int userId) {
        return exhibitDao.findExhibitsNotYetInQuestlog(userId);
    }
}
