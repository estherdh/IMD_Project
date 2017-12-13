package oose.p.c6.imd.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class QuestGeneratorTest {
    @Mock
    private QuestGeneratorFactory questGeneratorFactory;

    @InjectMocks
    private QuestGenerator questGenerator;

    @Mock
    private IQuestGenerator questGeneratorType;

    @Test
    public void generateQuest() {
        //init
        Mockito.when(questGeneratorFactory.getQuestGenerator(any(QuestTypes.class))).thenReturn(questGeneratorType);
        //test
        questGenerator.generateQuest(1);
        //verify
        Mockito.verify(questGeneratorFactory, times(1)).getQuestGenerator(any(QuestTypes.class));
        Mockito.verify(questGeneratorType, times(1)).generateQuest(anyInt());
    }
}
