package oose.p.c6.imd.domain;

public class ExhibitViewAction implements Action {
	private int exhibitId;

	public ExhibitViewAction(int exhibitId) {
		this.exhibitId = exhibitId;
	}

	public int getExhibitId() {
		return exhibitId;
	}
}
