package oose.p.c6.imd.domain;

import java.util.Map;

public class QrScanQuest implements IQuestType {
	private String QrString;

	public QrScanQuest(Map<String, String> properties) {
		this.QrString = properties.get("qrCode");
	}

	@Override
	public boolean checkQuestComplete(Action action) {
		if (action instanceof QrScanAction) {
			return ((QrScanAction) action).getQrCode().equals(QrString);
		} else {
			return false;
		}
	}
}
