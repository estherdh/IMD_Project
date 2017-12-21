package oose.p.c6.imd.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestTest {
	Quest quest;
	QrScanQuest mockStrategy;

	@Before
	public void setUp() {
		mockStrategy = mock(QrScanQuest.class);
		this.quest = new Quest(1, "questName", "questDescription", 100, 1, 0, 0, mockStrategy);
	}

	@Test
	public void checkQuestComplete() throws Exception {
		//init
		int expectedResult = 100;
		Action action = mock(Action.class);
		when(mockStrategy.checkQuestComplete(action)).thenReturn(true);
		//test
		int actualResult = quest.checkQuestComplete(action);
		//check
		assertThat(actualResult, is(expectedResult));
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