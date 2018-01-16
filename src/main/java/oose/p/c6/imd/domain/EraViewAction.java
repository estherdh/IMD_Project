package oose.p.c6.imd.domain;

public class EraViewAction implements Action {
	private int eraId;

	public EraViewAction(int eraId) {
		this.eraId = eraId;
	}

	public int getEraId() {
		return eraId;
	}
}
