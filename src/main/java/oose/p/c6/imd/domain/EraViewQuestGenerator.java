package oose.p.c6.imd.domain;

import java.util.List;
import java.util.Random;

public class EraViewQuestGenerator extends ViewQuestGenerator {

    public void generateQuest(int userId) {
        questTypeId = 4;

        List<Era> eras = findErasNotYetInQuestlog(userId);

        if(eras.size() > 0) {
            Era e = eras.get(new Random().nextInt(eras.size()));

            String key = "Era";
            String value = String.valueOf(e.getId());
            setProperties(key, value);

            addQuestToQuestlog(userId);
        }
    }

    private List<Era> findErasNotYetInQuestlog(int userId) {
        return exhibitDao.findErasNotYetInQuestlog(userId);
    }
}
