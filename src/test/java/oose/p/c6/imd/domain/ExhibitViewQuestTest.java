package oose.p.c6.imd.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExhibitViewQuestTest {
	private ExhibitViewQuest quest;
	@Before
	public void setUp() throws Exception {
		Map<String, String> input = new HashMap<>();
		input.put("Topstuk", "1");
		this.quest = new ExhibitViewQuest(input);

	}

	@Test
	public void checkQuestCompleteTestSuccess() throws Exception {
		//init
		Action inputAction = new ExhibitViewAction(1);
		//test
		boolean result = quest.checkQuestComplete(inputAction);
		//check
		assertTrue(result);
	}

	@Test
	public void checkQuestCompleteTestWrongExhibit() throws Exception {
		//init
		Action inputAction = new ExhibitViewAction(2);
		//test
		boolean result = quest.checkQuestComplete(inputAction);
		//check
		assertFalse(result);
	}

	@Test
	public void checkQuestCompleteTestWrongAction() throws Exception {
		//init
		Action inputAction = new DummyAction();
		//test
		boolean result = quest.checkQuestComplete(inputAction);
		//check
		assertFalse(result);
	}
}
