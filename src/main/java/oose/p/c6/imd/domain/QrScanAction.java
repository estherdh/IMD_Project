package oose.p.c6.imd.domain;

public class QrScanAction implements Action {
	private String qrCode;

	public QrScanAction(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getQrCode() {
		return qrCode;
	}
}
