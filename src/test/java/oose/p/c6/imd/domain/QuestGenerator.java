package oose.p.c6.imd.domain;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class QuestGenerator {

    @Mock
    private QuestGeneratorFactory questGeneratorFactory;

    @Test
    public void generateQuest() {
        QuestGenerator questGenerator = new QuestGenerator();
        questGenerator.generateQuest(1);

        Mockito.verify(questGeneratorFactory, times(1)).getQuestGenerator(any(QuestTypes.class));
    }
}
