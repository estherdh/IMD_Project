package oose.p.c6.imd.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExhibitViewQuestGenerator extends ViewQuestGenerator {

    public void generateQuest(int userId) {
        questTypeId = 3;

        List<Exhibit> exhibits = findExhibitsNotYetInQuestlog(userId);

        if (!exhibits.isEmpty()) {
            Exhibit e = exhibits.get(new Random().nextInt(exhibits.size()));

            setProperties("Topstuk", String.valueOf(e.getId()));
            setProperties("Tijdperk", String.valueOf(e.getEraId()));

            Era era = exhibitDao.findEra(userDao.find(userId), e.getEraId());
            valuesById.add(era.getName());
            valuesById.add(e.getName());

            addQuestToQuestlog(userId);
        }
    }

    private List<Exhibit> findExhibitsNotYetInQuestlog(int userId) {
        return exhibitDao.findExhibitsNotYetInQuestlog(userId);
    }
}
