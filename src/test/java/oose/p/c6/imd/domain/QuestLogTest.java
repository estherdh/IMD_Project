package oose.p.c6.imd.domain;

import javafx.beans.binding.When;
import oose.p.c6.imd.persistent.dao.IQuestDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestLogTest {
	@Mock
	IQuestDAO dao; //fce
	@InjectMocks
	QuestLog log;

	@Test
	public void checkQuestCompleteTestSuccess() {
		//init
		int expectedResult = 10;
		List<Quest> questList = new ArrayList<>();
		Quest quest = mock(Quest.class);
		questList.add(quest);
		when(quest.checkQuestComplete(any(Action.class))).thenReturn(expectedResult);
		when(dao.getQuestsForUser(1,1)).thenReturn(questList);
		//test
		int actualResult = log.checkQuestComplete(new QrScanAction("randomQr"), 1, 1);
		//check
		assertThat(actualResult, is(expectedResult));
	}

	@Test
	public void checkQuestCompleteTestMultipleQuestsComplete() {
		//init
		int expectedResult = 32;
		List<Quest> questList = new ArrayList<>();
		Quest quest1 = mock(Quest.class);
		Quest quest2 = mock(Quest.class);
		Quest quest3 = mock(Quest.class);
		questList.add(quest1);
		questList.add(quest2);
		questList.add(quest3);
		when(quest1.checkQuestComplete(any(Action.class))).thenReturn(10);
		when(quest2.checkQuestComplete(any(Action.class))).thenReturn(0);
		when(quest3.checkQuestComplete(any(Action.class))).thenReturn(22);
		when(dao.getQuestsForUser(1,1)).thenReturn(questList);
		//test
		int actualResult = log.checkQuestComplete(new QrScanAction("randomQr"), 1, 1);
		//check
		assertThat(actualResult, is(expectedResult));
	}

}