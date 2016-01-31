package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.json;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Connector;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 *  This class allows for aggregation.
 */
public class Aggregation implements IDeserialisableJson, IPersistable {
	private DataClass source;
	private DataClass target;
	private int sourceMultiplicity;
	private int targetMultiplicity;
	private Map<String, DataClass> dataClasses;

	@SuppressWarnings("unused") private JSONObject aggregationJson;

    public Aggregation(Map<String, DataClass> dataClasses) {
        this.dataClasses = dataClasses;
    }

	@Override public void initializeInstanceFromJson(final String element) {
		try {
			this.aggregationJson = new JSONObject(element);

			setSourceNode(this.aggregationJson.getLong("sourceNode"));
			setTargetNode(this.aggregationJson.getLong("targetNode"));
			setSourceMultiplicity(this.aggregationJson.getString("sourceMultiplicity"));
			setTargetMultiplicity(this.aggregationJson.getString("targetMultiplicity"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
		conn.insertAggregationIntoDatabase(this.source.getDataClassID(),
				this.target.getDataClassID(), this.sourceMultiplicity);
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
