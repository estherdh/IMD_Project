package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IQuestDAO;
import oose.p.c6.imd.persistent.dao.QuestJDBCDao;
import oose.p.c6.imd.persistent.dao.UserJDBCDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {
	@Mock
	private QuestJDBCDao questDao;
	private UserJDBCDao userDao;
	@InjectMocks
	private User user = new User(0, "mail", "password", "fullname", 0, 1);

	@Test
	public void checkQuestCompletedAddCoins() throws Exception {
		//init
		Action inputAction = mock(Action.class);
		QuestLog questLog = mock(QuestLog.class);
		user.setQuestLog(questLog);
		when(questLog.checkQuestComplete(inputAction, 0, 1)).thenReturn(100);
		//test
		boolean actualResult = user.checkQuestCompleted(inputAction);
		//check
		assertTrue(actualResult);
		assertThat(user.getCoins(), is(100));
	}

	@Test
	public void checkQuestQuestFailedNoCoins() throws Exception {
		//init
		Action inputAction = mock(Action.class);
		QuestLog questLog = mock(QuestLog.class);
		user.setQuestLog(questLog);
		when(questLog.checkQuestComplete(inputAction, 0, 1)).thenReturn(0);
		//test
		Boolean actualResult = user.checkQuestCompleted(inputAction);
		//check
		assertThat(user.getCoins(), is(0));
		assertFalse(actualResult);
	}

	@Test
	public void removeQuestFromBacklogSuccess() {
		//init
		User user = new User(1, "email", "password", "fullName", 10, 1);
		QuestLog questLog = mock(QuestLog.class);
		user.setQuestLog(questLog);
		when(questLog.removeQuestFromQuestLog(1, 1)).thenReturn(true);
		//test
		boolean actualResult = user.removeQuestFromQuestLog(1);
		//check
		System.out.println(actualResult);
		assertTrue(actualResult);
	}
}