import React, { Component } from "react";

import { getOrganization, getRoles } from "../API";

import NavBar from "./NavBar";

import CaseModelList from "./CaseModelList";
import SearchableList from "../components/SearchableList";

class OrganizationView extends Component {
  state = {
    casemodels: [],
    roles: ["1", "2", "3"],
    members: []
  };

  componentDidMount = async () => {
    let organization = await getOrganization();
    let roles = await getRoles();
    let members = organization.members.map(
      member => member.name + " (" + member.email + ")"
    );
    this.setState({
      casemodels: organization.casemodels,
      roles: roles,
      members: members
    });
  };

  render() {
    return (
      <React.Fragment>
        <NavBar />
        <ul className="nav nav-tabs" id="myTab" role="tablist">
          <li className="nav-item">
            <a
              className="nav-link active"
              id="casemodel-tab"
              data-toggle="tab"
              href="#casemodels"
              role="tab"
              aria-controls="home"
              aria-selected="true"
            >
              Case Models
            </a>
          </li>
          <li className="nav-item">
            <a
              className="nav-link"
              id="roles-tab"
              data-toggle="tab"
              href="#roles"
              role="tab"
              aria-controls="profile"
              aria-selected="false"
            >
              Roles
            </a>
          </li>
          <li className="nav-item">
            <a
              className="nav-link"
              id="members-tab"
              data-toggle="tab"
              href="#members"
              role="tab"
              aria-controls="contact"
              aria-selected="false"
            >
              Members
            </a>
          </li>
        </ul>
        <div className="tab-content" id="myTabContent">
          <div
            className="tab-pane fade show active"
            id="casemodels"
            role="tabpanel"
            aria-labelledby="casemodel-tab"
          >
            <CaseModelList casemodels={this.state.casemodels} />
          </div>
          <div
            className="tab-pane fade"
            id="roles"
            role="tabpanel"
            aria-labelledby="roles-tab"
          >
            <SearchableList elements={this.state.roles} />
          </div>
          <div
            className="tab-pane fade"
            id="members"
            role="tabpanel"
            aria-labelledby="members-tab"
          >
            <SearchableList elements={this.state.members} />
          </div>
        </div>
      </React.Fragment>
    );
  }
}

export default OrganizationView;
