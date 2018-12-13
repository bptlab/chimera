const URL = "http://localhost:8080/Chimera/api/v3/organizations";

function getOrganizationApiUrl() {
  return `${URL}/default`;
}

function getApiUrl() {
  return `${URL}/default/casemodels`;
}

export async function getCaseModel(cmId) {
  const URL = `${await getApiUrl()}/${cmId}`;
  let response = await getData(`${URL}`);
  return response;
}

export async function startCase(cmId, name) {
  const URL = `${await getApiUrl()}/${cmId}/cases`;
  const method = name ? "PUT" : "POST";
  const payload = { name: name };
  const response = await getData(URL, method, payload);
  return response;
}

export async function getOrganization() {
  const response = await getData(await getOrganizationApiUrl());
  return response;
}

export async function getRoles() {
  const response = await getData(`${await getOrganizationApiUrl()}/roles`);
  return response["roles"];
}

export async function deleteCaseModel(cmId) {
  const URL = `${await getApiUrl()}/${cmId}`;
  await getData(URL, "DELETE");
}

export async function getCase(cmId, caseId) {
  const URL = `${await getApiUrl()}/${cmId}/cases/${caseId}`;
  let response = await getData(URL);
  return response;
}

export async function getAvailableActivityInput(cmId, caseId, activityId) {
  const URL = `${await getApiUrl()}/${cmId}/cases/${caseId}/activities/${activityId}/availableInput`;
  const response = await getData(URL);
  return response["dataobjects"];
}

export async function getAvailableActivityOutput(cmId, caseId, activityId) {
  const URL = `${await getApiUrl()}/${cmId}/cases/${caseId}/activities/${activityId}/availableOutput`;
  const response = await getData(URL);
  return response["output"];
}

export async function beginActivity(cmId, caseId, activityId, selectedIds) {
  const URL = `${await getApiUrl()}/${cmId}/cases/${caseId}/activities/${activityId}/begin`;
  await getData(URL, "POST", selectedIds);
}

export async function terminateActivity(
  cmId,
  caseId,
  activityId,
  terminationValues
) {
  const URL = `${await getApiUrl()}/${cmId}/cases/${caseId}/activities/${activityId}/terminate`;
  await getData(URL, "POST", terminationValues);
}

export async function closeCase(cmId, caseId) {
  const URL = `${await getApiUrl()}/${cmId}/cases/${caseId}/terminate`;
  await getData(URL, "POST");
}

async function getData(url, method = "GET", body = null) {
  let request = {
    method: method,
    headers: {
      Authorization: "Basic YWRtaW46YWRtaW4=",
      "Content-Type": "application/json",
      Accept: "application/json"
    }
  };
  if (body !== null) {
    request.body = JSON.stringify(body);
  }
  const response = await fetch(url, request);
  return response.json();
}
