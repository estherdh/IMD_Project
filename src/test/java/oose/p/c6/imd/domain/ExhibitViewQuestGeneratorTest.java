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

    private List<Exhibit> expectedExhibitsNotAdded;
    private int userId;

    @Mock
    private IQuestDAO questDAO;
    @Mock
    private IExhibitDao exhibitDao;

    @InjectMocks
    private ExhibitViewQuestGenerator exhibitQuest;

    @Before
    public void init() {
        //init
        userId = 1;
        int questTypeId = 3;
        Map<String, String> expectedProperties = new HashMap<>();

        expectedExhibitsNotAdded = (new ArrayList<Exhibit>() {{
            add(new Exhibit(2, "Het voorbeeld beeldje", "Dit beeldje is ware kunst, een ideaal voorbeeld.", null, "object.png", 2010, 1, 1));
            add(new Exhibit(3, "Trekker", "Deze trekker is geen tractor!", null, "object.png", 2015, 1, 2));
            add(new Exhibit(4, "Voorbeeld streektaal", null, "Dit papier bevat een stuk tekst in streektaal: Oet de goaldn korenaarn skeup God de Tweantenaarn, en oet t kaf en d restn de leu oet t Westn", "object.png", 2017, 1, 2));
        }});
        Mockito.when(exhibitDao.findExhibitsNotYetInQuestlog(userId)).thenReturn(expectedExhibitsNotAdded);

        expectedProperties.put("Key", "Topstuk");
        expectedProperties.put("Value", expectedExhibitsNotAdded.get(0).getId() + "");

        questDAO.addQuestToQuestlog(expectedProperties, userId, questTypeId);
    }

    @Test
    public void generateQuestTest() {
        //init
        userId = 1;

        //test
        exhibitQuest.generateQuest(userId);

        List<Exhibit> actualExhibitsNotAdded = exhibitDao.findExhibitsNotYetInQuestlog(userId);

        //check
        assertEquals(expectedExhibitsNotAdded, actualExhibitsNotAdded);
    }

}
