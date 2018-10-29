import React, { Component } from "react";
import { Link } from "react-router-dom";
import PropTypes from "prop-types";

import { getRoles } from "../API";
import ExpandableOverview from "../components/ExpandableOverview";
import SearchField from "../components/SearchField"

class RolesList extends Component {
  state = {
    roles: [],
    displayedRoles : [],
  };

  componentDidMount = async () => {
    let roles = await getRoles();
    // per default all casemodels are collapsed
    this.setState({
      roles: roles,
      displayedRoles: roles
    });
  };

  filterRoles(filterString){
    let filteredRoles = this.state.roles.filter(role => role.includes(filterString));
    this.setState({
      roles: this.state.roles,
      displayedRoles: filteredRoles,})
  }

  render() {
    return (
      <React.Fragment>
        <SearchField searchHandler={filterString => this.filterRoles(filterString)}/>
        <main className="main-container">
          <div className="container">
            <ul className="list-group">
              {this.state.displayedRoles.map((role) =>
                <li className="list-group-item" key={role}>{role}</li>
              )}
            </ul>
          </div>
        </main>
      </React.Fragment>
    );
  }
}

export default RolesList;
