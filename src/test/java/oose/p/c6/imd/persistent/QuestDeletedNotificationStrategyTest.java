package oose.p.c6.imd.persistent;

import oose.p.c6.imd.domain.Quest;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IQuestDAO;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestDeletedNotificationStrategyTest {
    @Test
    public void createQuestDeletedNotificationText(){
        User u = new User(2, "test@void", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "muspi merol", 0, 2);
        Map m = new HashMap<String, String>();
        m.put("QuestId", "1");
        IQuestDAO dao = mock(IQuestDAO.class);
        DAOFactory.setQuestDao(dao);
        Quest q = mock(Quest.class);
        when(dao.find(anyInt(), any(User.class))).thenReturn(q);
        when(q.getName()).thenReturn("%Succesfully mocked this part!%");
        String result = new QuestDeletedNotificationStrategy().createNotificationText(u, "Hello {{{1}}} World", m);
        assertEquals("Hello %Succesfully mocked this part!% World", result);
    }
}
