package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class EraViewQuestGeneratorTest {

    private List<Era> actualEras;
    private List<Era> expectedEras;
    private Map<String, String> actualProperties;
    private Map<String, String> expectedProperties;
    private int userId;

    @Mock
    private IQuestDAO questDAO;
    @Mock
    private IExhibitDao exhibitDao;

    @InjectMocks
    private EraViewQuestGenerator eraQuest;

    @Test
    public void getEraListWhenNoQuestsAreAvailableTest() {
        //init
        userId = 1;

        expectedEras = (new ArrayList<Era>() {{
            add(new Era(1, "tijdperk topstuk"));
            add(new Era(3, "Steen tijd"));
            add(new Era(4, "Middeleeuwen"));
        }});
        Mockito.when(exhibitDao.findErasNotYetInQuestlog(userId, false)).thenReturn(expectedEras);

        //test
        actualEras = exhibitDao.findErasNotYetInQuestlog(userId, false);

        //check
        assertEquals(expectedEras, actualEras);
    }

    @Test
    public void getEraListWhenQuestsAreAvailableTest() {
        //init
        userId = 1;

        expectedEras = (new ArrayList<Era>() {{
            add(new Era(3, "Steen tijd"));
            add(new Era(4, "Middeleeuwen"));
        }});
        Mockito.when(exhibitDao.findErasNotYetInQuestlog(userId, true)).thenReturn(expectedEras);

        //test
        actualEras = exhibitDao.findErasNotYetInQuestlog(userId, true);

        //check
        assertEquals(expectedEras, actualEras);
    }

    @Test
    public void propertiesFromExhibitsWhenNoQuestsAvailableTest() {
        //init
        userId = 1;
        actualProperties = new HashMap<>();
        expectedProperties = new HashMap<>();

        expectedEras = (new ArrayList<Era>() {{
            add(new Era(1, "tijdperk topstuk"));
            add(new Era(3, "Steen tijd"));
            add(new Era(4, "Middeleeuwen"));
        }});
        Mockito.when(exhibitDao.findErasNotYetInQuestlog(userId, false)).thenReturn(expectedEras);
        actualEras = exhibitDao.findErasNotYetInQuestlog(userId, false);

        //test
        expectedProperties.put("Key", "Tijdperk");
        expectedProperties.put("Value", expectedEras.get(0).getId() + "");

        String key = "Tijdperk";
        int value = actualEras.get(0).getId();
        actualProperties.put("Key", key);
        actualProperties.put("Value", String.valueOf(value));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void propertiesFromExhibitsWhenQuestsAvailableTest() {
        //init
        userId = 1;
        actualProperties = new HashMap<>();
        expectedProperties = new HashMap<>();

        expectedEras = (new ArrayList<Era>() {{
            add(new Era(3, "Steen tijd"));
            add(new Era(4, "Middeleeuwen"));
        }});
        Mockito.when(exhibitDao.findErasNotYetInQuestlog(userId, true)).thenReturn(expectedEras);
        actualEras = exhibitDao.findErasNotYetInQuestlog(userId, true);

        //test
        expectedProperties.put("Key", "Tijdperk");
        expectedProperties.put("Value", expectedEras.get(0).getId() + "");

        String key = "Tijdperk";
        int value = actualEras.get(0).getId();
        actualProperties.put("Key", key);
        actualProperties.put("Value", String.valueOf(value));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void addQuestsToQuestlogWhenNoQuestsAvailableTest() {
        //init
        eraQuest.questTypeId = 4;
        actualProperties = new HashMap<>();

        List<Era> expectedErasNotAdded = (new ArrayList<Era>() {{
            add(new Era(3, "Steen tijd"));
            add(new Era(4, "Middeleeuwen"));
        }});
        Mockito.when(exhibitDao.findErasNotYetInQuestlog(userId, false)).thenReturn(expectedErasNotAdded);

        actualEras = exhibitDao.findErasNotYetInQuestlog(userId, false);
        String key = "Tijdperk";
        int value = actualEras.get(0).getId();
        actualProperties.put("Key", key);
        actualProperties.put("Value", String.valueOf(value));

        //test
        questDAO.addQuestToQuestlog(actualProperties, userId, eraQuest.questTypeId);
        List<Era> actualErasNotAdded = exhibitDao.findErasNotYetInQuestlog(userId, false);

        //check
        assertEquals(expectedErasNotAdded, actualErasNotAdded);
    }

    @Test
    public void addQuestsToQuestlogWhenQuestsAvailableTest() {
        //init
        eraQuest.questTypeId = 4;
        actualProperties = new HashMap<>();

        List<Era> expectedErasNotAdded = (new ArrayList<Era>() {{
            add(new Era(4, "Middeleeuwen"));
        }});
        Mockito.when(exhibitDao.findErasNotYetInQuestlog(userId, true)).thenReturn(expectedErasNotAdded);

        actualEras = exhibitDao.findErasNotYetInQuestlog(userId, true);
        String key = "Tijdperk";
        int value = actualEras.get(0).getId();
        actualProperties.put("Key", key);
        actualProperties.put("Value", String.valueOf(value));

        //test
        questDAO.addQuestToQuestlog(actualProperties, userId, eraQuest.questTypeId);
        List<Era> actualErasNotAdded = exhibitDao.findErasNotYetInQuestlog(userId, true);

        //check
        assertEquals(expectedErasNotAdded, actualErasNotAdded);
    }

    @Test
    public void areQuestsAvailableTest() {
        //init
        userId = 1;
        boolean questsAreActuallyAvailable;
        boolean questsAreExpectedAvailable = true;

        //test
        questsAreActuallyAvailable = (exhibitDao.findErasNotYetInQuestlog(userId, true).size() <= 0);

        //check
        assertEquals(questsAreExpectedAvailable, questsAreActuallyAvailable);
    }
}
