package oose.p.c6.imd.domain;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class QuestFactoryTest {
    @Test
    public void QuestFactoryCreateQrQuest(){
        QuestFactory qf = QuestFactory.getInstance();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("QR","ItWorked");

        IQuestType q = qf.generateQuest(QuestTypes.QRCODESCAN, properties);

        assertThat(q, instanceOf(QrScanQuest.class));
    }

    @Test
    public void QuestFactoryCreateDefault(){
        QuestFactory qf = QuestFactory.getInstance();
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("QR","ItWorked");
        IQuestType q = qf.generateQuest(QuestTypes.GENERICTEXT, properties);

        assertThat(q, instanceOf(DummyQuest.class));
    }

}

