package oose.p.c6.imd.domain;

import java.util.List;
import java.util.Random;

public class EraViewQuestGenerator extends BaseQuestGenerator {

    public void generateQuest(int userId) {
        questTypeId = 4;

        List<Era> eras = findErasNotYetInQuestlog(userId);

        if (!eras.isEmpty()) {
            Era e = eras.get(new Random().nextInt(eras.size()));

            setProperties("Tijdperk", String.valueOf(e.getId()));

            valuesById.add(e.getName());

            addQuestToQuestlog(userId);
        }
    }

    private List<Era> findErasNotYetInQuestlog(int userId) {
        return exhibitDao.findErasNotYetInQuestlog(userId);
    }
}
