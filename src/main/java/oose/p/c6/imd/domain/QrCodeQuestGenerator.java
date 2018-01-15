package oose.p.c6.imd.domain;

import java.util.List;
import java.util.Random;

public class QrCodeQuestGenerator extends BaseQuestGenerator {
    public void generateQuest(int userId) {
        questTypeId = 4;

        List<Museum> museums = findMuseumsNotYetInQuestlog(userId);

        if (!museums.isEmpty()) {
            Museum m = museums.get(new Random().nextInt(museums.size()));

            setProperties("Qr", String.valueOf(m.getQrCode()));
            valuesById.add(m.getName());

            addQuestToQuestlog(userId);
        }
    }

    private List<Museum> findMuseumsNotYetInQuestlog(int userId) {
        return exhibitDao.findMuseumsNotYetInQuestlog(userId);
    }
}
