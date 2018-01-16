package oose.p.c6.imd.domain;

import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestGeneratorFactory {
    private static final Logger LOGGER = Logger.getLogger(QuestGeneratorFactory.class.getName());
    private static QuestGeneratorFactory instance;

    public static QuestGeneratorFactory getInstance() {
        if(instance == null) {
            return new QuestGeneratorFactory();
        }
        return instance;
    }

    public BaseQuestGenerator getQuestGenerator(QuestTypes questType) {
        switch(questType) {
            case EXHIBITVIEW:
                return new ExhibitViewQuestGenerator();
            case ERAVIEW:
                return new EraViewQuestGenerator();
            case QRCODESCAN:
                return new QrCodeQuestGenerator();
            default:
                LOGGER.log(Level.WARNING, "Geen generator voor questtype: "+questType+" gevonden");
                return null;
        }
    }

    public static void setInstance(QuestGeneratorFactory instance) {
        QuestGeneratorFactory.instance = instance;
    }
}
