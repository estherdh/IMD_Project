package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;
import org.junit.Before;
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
public class ExhibitViewQuestGeneratorTest {

    private List<Exhibit> actualExhibits;
    private List<Exhibit> expectedExhibits;
    private Map<String, String> actualProperties;
    private Map<String, String> expectedProperties;
    int userId;

    @Mock
    private IQuestDAO questDAO;
    @Mock
    private IExhibitDao exhibitDao;

    @InjectMocks
    private ExhibitViewQuestGenerator exhibitQuest;

    @Before
    public void initAndTest() {
        //init
        userId = 1;
        actualProperties = new HashMap<>();

        expectedExhibits = (new ArrayList<Exhibit>() {{
            add(new Exhibit(1, "Het test object", "Dit object wordt altijd al gebruikt om te testen", null, "object.png", 1999, 1, 1));
            add(new Exhibit(2, "Het voorbeeld beeldje", "Dit beeldje is ware kunst, een ideaal voorbeeld.", null, "object.png", 2010, 1, 1));
            add(new Exhibit(3, "Trekker", "Deze trekker is geen tractor!", null, "object.png", 2015, 1, 2));
            add(new Exhibit(4, "Voorbeeld streektaal", null, "Dit papier bevat een stuk tekst in streektaal: Oet de goaldn korenaarn skeup God de Tweantenaarn, en oet t kaf en d restn de leu oet t Westn", "object.png", 2017, 1, 2));
        }});
        Mockito.when(exhibitDao.findExhibitsNotYetInQuestlog(userId)).thenReturn(expectedExhibits);
        expectedProperties = new HashMap<>();
        expectedProperties.put("Key", "Topstuk");
        expectedProperties.put("Value", expectedExhibits.get(0).getId() + "");

        //test
        actualExhibits = exhibitDao.findExhibitsNotYetInQuestlog(userId);

        String key = "Topstuk";
        int value = actualExhibits.get(0).getId();
        actualProperties.put("Key", key);
        actualProperties.put("Value", String.valueOf(value));
    }

    @Test
    public void getExhibitsListTest() {
        //check
        assertEquals(expectedExhibits, actualExhibits);
    }

    @Test
    public void propertiesFromExhibitsTest() {
        //check
        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void addQuestsToQuestlogTest() {
        //init
        exhibitQuest.questTypeId = 3;
        List<Exhibit> expectedExhibitsNotAdded = (new ArrayList<Exhibit>() {{
            add(new Exhibit(2, "Het voorbeeld beeldje", "Dit beeldje is ware kunst, een ideaal voorbeeld.", null, "object.png", 2010, 1, 1));
            add(new Exhibit(3, "Trekker", "Deze trekker is geen tractor!", null, "object.png", 2015, 1, 2));
            add(new Exhibit(4, "Voorbeeld streektaal", null, "Dit papier bevat een stuk tekst in streektaal: Oet de goaldn korenaarn skeup God de Tweantenaarn, en oet t kaf en d restn de leu oet t Westn", "object.png", 2017, 1, 2));
        }});
        Mockito.when(exhibitDao.findExhibitsNotYetInQuestlog(userId)).thenReturn(expectedExhibitsNotAdded);

        //test
        questDAO.addQuestToQuestlog(actualProperties, userId, exhibitQuest.questTypeId);

        List<Exhibit> actualExhibitsNotAdded = exhibitDao.findExhibitsNotYetInQuestlog(userId);

        //check
        assertEquals(expectedExhibitsNotAdded, actualExhibitsNotAdded);
    }

}
