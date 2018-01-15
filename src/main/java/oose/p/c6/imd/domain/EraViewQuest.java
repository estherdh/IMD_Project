package oose.p.c6.imd.domain;

import java.util.Map;

public class EraViewQuest implements IQuestType {
	private int expectedEraId;
	public EraViewQuest(Map<String, String> properties) {
		this.expectedEraId = Integer.parseInt(properties.get("Era"));
	}
	@Override
	public boolean checkQuestComplete(Action action) {
		if (action instanceof EraViewAction) {
			return ((EraViewAction) action).getEraId() == expectedEraId;
		} else {
			return false;
		}
	}
}
