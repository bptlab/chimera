package de.hpi.bpt.chimera.rest.json;

import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;

import de.hpi.bpt.chimera.model.CaseModel;
import de.hpi.bpt.chimera.usermanagment.Organization;
import de.hpi.bpt.chimera.usermanagment.User;
import net.minidev.json.JSONArray;

public class OrganizationJSONObject extends JSONObject {
	public OrganizationJSONObject(Organization organization, UriInfo uri) {
		JSONObject data = new JSONObject();
		put("data", data);
		data.put("type", "organizations");
		data.put("id", organization.getId());

		JSONObject links = new JSONObject();
		put("links", links);
		links.put("self", uri.getAbsolutePath());

		JSONObject attributes = new JSONObject();
		data.put("attributes", attributes);
		attributes.put("name", organization.getName());
		attributes.put("description", organization.getDescription());

		JSONObject relationships = new JSONObject();
		relationships.put("casemodels", createCaseModelsRelationshipJSONObject(organization, uri));
		relationships.put("members", createMembersRelationshipJSONObject(organization, uri));
		data.put("relationships", relationships);

		JSONArray included = new JSONArray();
		for (CaseModel casemodel : organization.getCaseModels().values()) {
			JSONObject cm = new JSONObject();

			cm.put("type", "casemodels");
			cm.put("id", casemodel.getId());

			JSONObject cmAttributes = new JSONObject();
			cm.put("attributes", cmAttributes);
			cmAttributes.put("name", casemodel.getName());
			cmAttributes.put("deployment", casemodel.getDeployment().toString());

			JSONObject cmLinks = new JSONObject();
			cm.put("links", cmLinks);
			cmLinks.put("self", String.format("%s/casemodels/%s", uri.getAbsolutePath(), casemodel.getId()));

			included.add(casemodel);
		}

		for (User member : organization.getMembers().values()) {
			JSONObject m = new JSONObject();

			m.put("type", "members");
			m.put("id", member.getId());

			JSONObject memberAttributes = new JSONObject();
			m.put("attributes", memberAttributes);
			memberAttributes.put("name", member.getName());
			memberAttributes.put("email", member.getEmail());

			JSONObject mLinks = new JSONObject();
			m.put("links", mLinks);
			mLinks.put("self", String.format("%s/members/%s", uri.getAbsolutePath(), member.getId()));

			included.add(m);
		}
		put("included", included);
	}

	private JSONObject createCaseModelsRelationshipJSONObject(Organization organization, UriInfo uri) {
		JSONObject casemodels = new JSONObject();

		JSONObject links = new JSONObject();
		casemodels.put("links", links);
		links.put("self", String.format("%s/relationships/casemodels", uri.getAbsolutePath()));
		links.put("related", String.format("%s/casemodels", uri.getAbsolutePath()));

		JSONArray data = new JSONArray();
		for (CaseModel cm : organization.getCaseModels().values()) {
			JSONObject cmData = new JSONObject();
			cmData.put("type", "casemodels");
			cmData.put("id", cm.getId());
			// TODO: think whether additional links should be added here
			data.add(cmData);
		}
		casemodels.put("data", data);

		return casemodels;
	}

	private JSONObject createMembersRelationshipJSONObject(Organization organization, UriInfo uri) {
		JSONObject members = new JSONObject();

		JSONObject links = new JSONObject();
		members.put("links", links);
		links.put("self", String.format("%s/relationships/members", uri.getAbsolutePath()));
		links.put("related", String.format("%s/members", uri.getAbsolutePath()));

		JSONArray data = new JSONArray();
		for (User member : organization.getMembers().values()) {
			JSONObject memberData = new JSONObject();
			memberData.put("type", "members");
			memberData.put("id", member.getId());
			// TODO: think whether additional links should be added here
			data.add(memberData);
		}
		members.put("data", data);

		return members;
	}
}

