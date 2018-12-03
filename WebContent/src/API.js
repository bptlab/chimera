const URL = "http://localhost:8080/Chimera/api/v3/organizations";

let API_URL;

async function getApiUrl() {
  if (API_URL) {
    return API_URL;
  }

  const response = await getData(`${URL}`);
  const organizations = response["organizations"].filter(function(org) {
    return org.name === "Default";
  });

  const orgId = organizations[0].id;
  API_URL = `${URL}/${orgId}/casemodels`;
  return API_URL;
}

//Think about making the API_URL without the /casemodels so that we do not need this convenient function
async function getOrganizationApiUrl() {
  //return API_URL without /casemodels in the end
  let api_url = await getApiUrl();
  return await api_url.substr(0, api_url.length - 11);
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

//@depricated
export async function getCaseModels() {
  const URL = `${await getApiUrl()}`;
  const response = await getData(`${URL}`);
  return response["casemodels"];
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
  const response2 = await getData(`${URL}/activities?state=ready`);
  const response3 = await getData(`${URL}/activities?state=running`);
  response.activities = {
    ready: response2.activities,
    running: response3.activities
  };
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

export async function getFragments(cmId) {
  const URL = `${await getApiUrl()}/${cmId}/xml`;
  const response = await getData(URL);
  return response["bpmn"];
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
