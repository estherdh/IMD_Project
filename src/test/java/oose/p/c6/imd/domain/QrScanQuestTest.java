package oose.p.c6.imd.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class QrScanQuestTest {
	private QrScanQuest quest;

	@Before
	public void setUp() throws Exception {
		Map<String, String> input = new HashMap<>();
		input.put("qrCode", "qrCodeTest");
		this.quest = new QrScanQuest(input);

	}

	@Test
	public void checkQuestCompleteTestSuccess() throws Exception {
		//init
		Action inputAction = new QrScanAction("qrCodeTest");
		//test
		boolean result = quest.checkQuestComplete(inputAction);
		//check
		assertTrue(result);
	}

	@Test
	public void checkQuestCompleteTestWrongQrCode() throws Exception {
		//init
		Action inputAction = new QrScanAction("aWrongQrCode");
		//test
		boolean result = quest.checkQuestComplete(inputAction);
		//check
		assertFalse(result);
	}

//	TODO: Wanneer er een tweede actie is hier checken of je ook false krijgt wanneer er geen qrCodeAction doorheen komt.
//	@Test
//	public void checkQuestCompleteTestWrongAction() throws Exception {
//		//init
//		Action inputAction = new QrScanAction("aWrongQrCode");
//		//test
//		boolean result = quest.checkQuestComplete(inputAction);
//		//check
//		assertFalse(result);
//	}

}