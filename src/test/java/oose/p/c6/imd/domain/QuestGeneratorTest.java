package oose.p.c6.imd.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class QuestGeneratorTest {

    private IQuestGenerator questGeneratorType;

    @Before
    public void setUp() {
        questGeneratorType = Mockito.mock(IQuestGenerator.class);
    }

    @Test
    public void generateQuest() {
        //init
        Mockito.doNothing().when(questGeneratorType).generateQuest(anyInt());
        //test
        QuestGenerator questGenerator = new QuestGenerator();
        questGenerator.setQuestGeneratorType(questGeneratorType);
        questGenerator.generateQuest(1);
        //verify
        Mockito.verify(questGeneratorType, times(1)).generateQuest(anyInt());
    }
}
