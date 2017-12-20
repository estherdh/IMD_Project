package oose.p.c6.imd.persistent;

import oose.p.c6.imd.domain.Notification;
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

public class NotificationCreatorTest {
    @Test
    public void testQuestCompletedNotification(){
        User u = new User(2, "test@void", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "muspi merol", 0, 2);
        Map m = new HashMap<String, String>();
        m.put("QuestId", "1");
        m.put("Coins", "7898");
        IQuestDAO dao = mock(IQuestDAO.class);
        DAOFactory.setQuestDao(dao);
        Quest q = mock(Quest.class);
        when(dao.find(anyInt(), any(User.class))).thenReturn(q);
        when(q.getName()).thenReturn("%Succesfully mocked this part!%");
        Notification result = NotificationCreator.createNotification(u, "Test: {{{1}}} Coins: {{{2}}}", m, 1,"DATE", true,12);
        assertEquals(result.getRead(), true);
        assertEquals(result.getText(), "Test: %Succesfully mocked this part!% Coins: 7898");
        assertEquals(result.getTime(), "DATE");
        assertEquals(result.getTypeId(), 1);
        assertEquals(result.getId(), 12);
    }

    @Test
    public void testQuestDeletedNotification(){
        User u = new User(2, "test@void", "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "muspi merol", 0, 2);
        Map m = new HashMap<String, String>();
        m.put("QuestId", "1");
        IQuestDAO dao = mock(IQuestDAO.class);
        DAOFactory.setQuestDao(dao);
        Quest q = mock(Quest.class);
        when(dao.find(anyInt(), any(User.class))).thenReturn(q);
        when(q.getName()).thenReturn("%Succesfully mocked this part!%");
        Notification result = NotificationCreator.createNotification(u, "Result: {{{1}}}", m, 2,"DATE", true,14);
        assertEquals(result.getRead(), true);
        assertEquals(result.getText(), "Result: %Succesfully mocked this part!%");
        assertEquals(result.getTime(), "DATE");
        assertEquals(result.getTypeId(), 2);
        assertEquals(result.getId(), 14);
    }

}
