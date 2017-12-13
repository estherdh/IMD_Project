package oose.p.c6.imd.domain;

import java.util.concurrent.ThreadLocalRandom;

public class QuestGenerator {

    private IQuestGenerator questGeneratorType;

    public void setQuestGeneratorType(IQuestGenerator questGeneratorType) {
        this.questGeneratorType = questGeneratorType;
    }

    public void generateQuest(int userId) {
        int random = ThreadLocalRandom.current().nextInt(1, QuestTypes.values().length);
        QuestTypes questType = QuestTypes.values()[random];
        if(questGeneratorType == null) {
            questGeneratorType = QuestGeneratorFactory.getInstance().getQuestGenerator(questType);
        }
        questGeneratorType.generateQuest(userId);
    }
}
