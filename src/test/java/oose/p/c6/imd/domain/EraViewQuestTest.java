package oose.p.c6.imd.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EraViewQuestTest {
	private EraViewQuest quest;
	@Before
	public void setUp() throws Exception {
		Map<String, String> input = new HashMap<>();
		input.put("Tijdperk", "1");
		this.quest = new EraViewQuest(input);

	}

	@Test
	public void checkQuestCompleteTestSuccess() throws Exception {
		//init
		Action inputAction = new EraViewAction(1);
		//test
		boolean result = quest.checkQuestComplete(inputAction);
		//check
		assertTrue(result);
	}

	@Test
	public void checkQuestCompleteTestWrongQrCode() throws Exception {
		//init
		Action inputAction = new EraViewAction(2);
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
