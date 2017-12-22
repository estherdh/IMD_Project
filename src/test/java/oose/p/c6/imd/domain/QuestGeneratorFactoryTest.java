package oose.p.c6.imd.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class QuestGeneratorFactoryTest {
    @Test
    public void getQuestGeneratorEraViewQuestGeneratorTest(){
        QuestGeneratorFactory questGeneratorFactory = QuestGeneratorFactory.getInstance();
        BaseQuestGenerator questGenerator = questGeneratorFactory.getQuestGenerator(QuestTypes.ERAVIEW);
        assertThat(questGenerator, instanceOf(EraViewQuestGenerator.class));
    }

    @Test
    public void getQuestGeneratorExhibitViewQuestGeneratorTest(){
        QuestGeneratorFactory questGeneratorFactory = QuestGeneratorFactory.getInstance();
        BaseQuestGenerator questGenerator = questGeneratorFactory.getQuestGenerator(QuestTypes.EXHIBITVIEW);
        assertThat(questGenerator, instanceOf(ExhibitViewQuestGenerator.class));
    }

    @Test
    public void getQuestGeneratorDefaultTest(){
        QuestGeneratorFactory questGeneratorFactory = QuestGeneratorFactory.getInstance();
        BaseQuestGenerator questGenerator = questGeneratorFactory.getQuestGenerator(QuestTypes.QRCODESCAN);
        assertThat(questGenerator, is(nullValue()));
    }
}
