package oose.p.c6.imd.domain;

import java.util.Map;

public class ExhibitViewQuest implements IQuestType {
	private int expectedExhibtiId;

	public ExhibitViewQuest(Map<String, String> properties) {
		this.expectedExhibtiId = Integer.parseInt(properties.get("Topstuk"));
	}
	@Override
	public boolean checkQuestComplete(Action action) {
		if (action instanceof ExhibitViewAction) {
			return ((ExhibitViewAction) action).getExhibitId() == expectedExhibtiId;
		} else {
			return false;
		}
	}
}
