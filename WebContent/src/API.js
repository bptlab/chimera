const API_URL =
  "http://localhost:8080/Chimera/api/v3/organizations/e7754ea47ec643d1aeac8f5e2529eed7/casemodels";

export async function getCaseModel(cmId) {
  let response = await getData(`${API_URL}/${cmId}`);
  return response;
}

export async function startCase(cmId, name) {
  const URL = `${API_URL}/${cmId}/cases`;
  const method = name ? "PUT" : "POST";
  const payload = { name: name };
  const response = await getData(URL, method, payload);
  return response;
}

export async function getCaseModels() {
  const response = await getData(API_URL);
  return response["casemodels"];
}

export async function deleteCaseModel(cmId) {
  const URL = `${API_URL}/${cmId}`;
  await getData(URL, "DELETE");
}

export async function getCase(cmId, caseId) {
  const URL = `${API_URL}/${cmId}/cases/${caseId}`;
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
  const URL = `${API_URL}/${cmId}/cases/${caseId}/activities/${activityId}/availableInput`;
  const response = await getData(URL);
  return response["dataobjects"];
}

export async function getAvailableActivityOutput(cmId, caseId, activityId) {
  const URL = `${API_URL}/${cmId}/cases/${caseId}/activities/${activityId}/availableOutput`;
  const response = await getData(URL);
  return response;
}

export async function beginActivity(cmId, caseId, activityId) {
  const URL = `${API_URL}/${cmId}/cases/${caseId}/activities/${activityId}/begin`;
  await getData(URL, "POST", []);
}

export async function closeCase(cmId, caseId) {
  const URL = `${API_URL}/${cmId}/cases/${caseId}/terminate`;
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
