package oose.p.c6.imd.domain;

import java.util.concurrent.ThreadLocalRandom;

public class QuestGenerator {
    public void generateQuest(int userId) {
        int random = ThreadLocalRandom.current().nextInt(1, QuestTypes.values().length);
        QuestTypes questType = QuestTypes.values()[random];
        IQuestGenerator questGenerator = QuestGeneratorFactory.getInstance().getQuestGenerator(questType);
        questGenerator.generateQuest(userId);
    }
}
