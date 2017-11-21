package oose.p.c6.imd.domain;

import java.util.Map;

public class QuestFactory {
	private static QuestFactory instance;

	private QuestFactory() {
		//empty
	}

	public static QuestFactory getInstance() {
		if (instance == null) {
			instance = new QuestFactory();
		}
		return instance;
	}

	public IQuestType generateQuest(QuestTypes type, Map<String, String> properties) {
		switch (type) {
			case QRCODESCAN:
				return new QrScanQuest(properties);
			default:
				System.out.println("HET QUESTTYPE " + type + " IS NIET GEVONDEN IN 'QuestFactory - generateQuests()'");
				return null;
		}
	}
}
