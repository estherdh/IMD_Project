package oose.p.c6.imd.domain;

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

	public Quest generateQuest(QuestTypes type) {
		switch (type) {
			case QRCODESCAN:
				return new Quest();
			default:
				System.out.println("HET QUESTTYPE " + type + " IS NIET GEVONDEN IN 'QuestFactory - generateQuests()'");
				return null;
		}
	}
}
