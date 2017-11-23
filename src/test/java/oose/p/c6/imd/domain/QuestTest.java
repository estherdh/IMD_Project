package oose.p.c6.imd.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestTest {
	Quest quest;
	QrScanQuest mockStrategy;

	@Before
	public void setUp() {
		mockStrategy = mock(QrScanQuest.class);
		this.quest = new Quest(1, "questName", "questDescription", 100, mockStrategy);
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

}