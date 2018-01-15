package oose.p.c6.imd.domain;

import java.util.concurrent.ThreadLocalRandom;

public class QuestGenerator {
    private QuestGeneratorFactory questGeneratorFactory;

    public QuestGenerator() {
        questGeneratorFactory = QuestGeneratorFactory.getInstance();
    }

    public void generateQuest(int userId) {
        int random = ThreadLocalRandom.current().nextInt(0, QuestTypes.values().length);
        QuestTypes questType = QuestTypes.values()[random];
        BaseQuestGenerator questGeneratorType = questGeneratorFactory.getQuestGenerator(questType);
        if(questGeneratorType != null) {
            questGeneratorType.generateQuest(userId);
        }
    }
}
