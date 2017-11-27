package oose.p.c6.imd.domain;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestFactory {
	private static final Logger LOGGER = Logger.getLogger(QuestFactory.class.getName());
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
				LOGGER.log(Level.WARNING, "HET QUESTTYPE " + type + " IS NIET GEVONDEN IN 'QuestFactory - generateQuests()'");
				return new DummyQuest();
		}
	}

	public static void setFactory(QuestFactory factory) {
		instance = factory;
	}
}
