package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IUserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuestTest {
	@Mock
	private IUserDao userDao;
	@Mock
	private QrScanQuest mockStrategy;

	private Quest quest;

	@Before
	public void setUp() {
		mockStrategy = mock(QrScanQuest.class);
		this.quest = new Quest(1, "questName", "questDescription", 100, 1, 0, 0, mockStrategy);
		DAOFactory.setUserDao(userDao);
	}

	@Test
	public void checkQuestComplete() throws Exception {
		//init

		int expectedResult = 100;
		Action action = mock(Action.class);
		User mockUser = mock(User.class);
		when(mockStrategy.checkQuestComplete(action)).thenReturn(true);
		when(userDao.getUserByQuestId(anyInt())).thenReturn(mockUser);
		when(mockUser.getCoins()).thenReturn(0);

		//test
		int actualResult = quest.checkQuestComplete(action);
		//check
		assertThat(actualResult, is(expectedResult));
		verify(userDao, times(1)).getUserByQuestId(anyInt());
		verify(userDao, times(1)).addNotification(anyInt(), any(), any());
	}

	@Test
	public void checkQuestFailed() throws Exception {
		//init
		int expectedResult = 0;
		Action action = mock(Action.class);
		when(mockStrategy.checkQuestComplete(action)).thenReturn(false);
		//test
		int actualResult = quest.checkQuestComplete(action);
		//check
		assertThat(actualResult, is(expectedResult));
	}

}