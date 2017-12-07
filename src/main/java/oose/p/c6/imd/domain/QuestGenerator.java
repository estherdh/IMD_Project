package oose.p.c6.imd.domain;

import java.util.concurrent.ThreadLocalRandom;

public class QuestGenerator {
    public void generateQuest(int userId) {
        int random = ThreadLocalRandom.current().nextInt(1, QuestTypes.values().length);
        QuestTypes questType = QuestTypes.values()[random];
        IQuestGenerator questGeneratorType = QuestGeneratorFactory.getInstance().getQuestGenerator(questType);
        questGeneratorType.generateQuest(userId);
    }
}
