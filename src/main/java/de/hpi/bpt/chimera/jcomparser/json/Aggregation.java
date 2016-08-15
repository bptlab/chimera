package de.hpi.bpt.chimera.jcomparser.json;

import de.hpi.bpt.chimera.jcomparser.saving.Connector;
import de.hpi.bpt.chimera.jcomparser.saving.IPersistable;

import java.util.Map;

/**
 *  This class allows for aggregation.
 */
public class Aggregation implements IPersistable {
	//TODO how to handle this
	private DataClass source;
	private DataClass target;
	private int sourceMultiplicity;
	private int targetMultiplicity;
	private Map<String, DataClass> dataClasses;

    public Aggregation(Map<String, DataClass> dataClasses) {
        this.dataClasses = dataClasses;
    }


	private void setSourceMultiplicity(String value) {
		String[] multiplicity = value.split("\\.\\.");
		if (multiplicity.length != 0) {
			if (multiplicity[multiplicity.length - 1].equals("*")) {
				this.sourceMultiplicity = Integer.MAX_VALUE;
			} else {
				this.sourceMultiplicity = Integer.parseInt(
						multiplicity[multiplicity.length - 1]);
			}
		}
	}

	public void setTargetMultiplicity(String value) {
		String[] multiplicity = value.split("\\.\\.");
		if (multiplicity.length != 0) {
			if (multiplicity[multiplicity.length - 1].equals("*")) {
				this.targetMultiplicity = Integer.MAX_VALUE;
			} else {
				this.targetMultiplicity = Integer.parseInt(
						multiplicity[multiplicity.length - 1]);
			}
		}
	}

	private void setTargetNode(Long value) {
		if (value != null && this.dataClasses != null) {
			this.target = this.dataClasses.get(value);
		}
	}

	private void setSourceNode(Long value) {
		if (value != null && this.dataClasses != null) {
			this.source = this.dataClasses.get(value);
		}
	}

	@Override public int save() {
		Connector conn = new Connector();
		conn.insertAggregation(this.source.getDatabaseId(),
				this.target.getDatabaseId(), this.sourceMultiplicity);
		return 1;
	}

	public Map<String, DataClass> getDataClasses() {
		return dataClasses;
	}

	public DataClass getSource() {
		return source;
	}

	public DataClass getTarget() {
		return target;
	}

	public int getSourceMultiplicity() {
		return sourceMultiplicity;
	}

	public int getTargetMultiplicity() {
		return targetMultiplicity;
	}
}
