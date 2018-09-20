package de.hpi.bpt.chimera.rest.json;

import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;

import de.hpi.bpt.chimera.model.CaseModel;

public class CaseModelJSONObject extends JSONObject {
	public CaseModelJSONObject(CaseModel cm, UriInfo uri) {
		JSONObject data = new JSONObject();
		data.put("type", "casemodel");
		data.put("id", cm.getId());
		put("data", data);

	}
}
