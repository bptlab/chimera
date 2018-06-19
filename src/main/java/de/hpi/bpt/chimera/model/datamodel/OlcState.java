package de.hpi.bpt.chimera.model.datamodel;

public class OlcState {
	private final String label;
	private final Olc olc;
	
	public OlcState(String label, Olc olc) {
		this.label = label;
		this.olc = olc;
	}
	
	public String getLabel() {
		return label;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof OlcState) {
			return ((OlcState) o).getLabel().equals(label) &&
					((OlcState) o).olc.getDataClass().equals(olc.getDataClass());
		}
		return false;
	}

}
