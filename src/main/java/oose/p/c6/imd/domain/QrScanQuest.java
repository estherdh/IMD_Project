package oose.p.c6.imd.domain;

import java.util.Map;

public class QrScanQuest implements IQuestType {
	private String qrString;

	public QrScanQuest(Map<String, String> properties) {
		this.qrString = properties.get("QR");
	}

	@Override
	public boolean checkQuestComplete(Action action) {
		if (action instanceof QrScanAction) {
			return ((QrScanAction) action).getQrCode().equals(qrString);
		} else {
			return false;
		}
	}
}
