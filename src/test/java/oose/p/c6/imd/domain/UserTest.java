package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.QuestJDBCDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {
	@Mock
	private QuestJDBCDao questDao;
	@InjectMocks
	private User user = new User(0, "mail", "password", "fullname", 0);

	@Test
	public void checkQuestCompletedAddCoins() throws Exception {
		//init
		Action inputAction = mock(Action.class);
		QuestLog questLog = mock(QuestLog.class);
		user.setQuestLog(questLog);
		when(questLog.checkQuestComplete(any(Action.class))).thenReturn(100);
		//test
		user.checkQuestCompleted(inputAction);
		//check
		assertThat(user.getCoins(), is(100));
	}

	@Test
	public void checkQuestQuestFailedNoCoins() throws Exception {
		//init
		Action inputAction = mock(Action.class);
		QuestLog questLog = mock(QuestLog.class);
		user.setQuestLog(questLog);
		when(questLog.checkQuestComplete(any(Action.class))).thenReturn(0);
		//test
		user.checkQuestCompleted(inputAction);
		//check
		assertThat(user.getCoins(), is(0));
	}

	//TODO fix deze functie, werkt op het moment niet omdmat de rest nog niet is geïmplementeerd.
	@Test
	public void removeQuestTestSuccess() {
		//init
//		User user = new User();
//		user.setId(1);
//		when(questDao.removeQuest(1, 1)).thenReturn(true);
		//test
//		Response actualResult = user.removeQuest(1);
		//check
//		assertThat(actualResult.getStatus(), is(200));
	}
}