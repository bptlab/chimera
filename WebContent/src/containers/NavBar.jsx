import React, { Component } from "react";

class NavBar extends Component {
  url = () => {
    return (
      window.location.protocol +
      "//" +
      window.location.hostname +
      (window.location.port && ":" + window.location.port) +
      (process.env.REACT_APP_ROUTER_BASE || "")
    );
  };

  CaseModelBreadCrump = casemodel => {
    if (casemodel) {
      return (
        <li className="breadcrumb-item">
          <a
            aria-current="page"
            href={this.url() + "/casemodels/" + casemodel.id}
          >
            {casemodel.name}
          </a>
        </li>
      );
    }
  };

  CaseBreadCrump = (casemodel, caze) => {
    if (caze) {
      return (
        <li className="breadcrumb-item">
          <a
            aria-current="page"
            href={
              this.url() + "/casemodels/" + casemodel.id + "/cases/" + caze.id
            }
          >
            {caze.name}
          </a>
        </li>
      );
    }
  };

  render() {
    const { casemodel } = this.props;
    const { caze } = this.props;
    return (
      <header>
        <nav aria-label="breadcrumb">
          <ol className="breadcrumb m-0">
            <li className="breadcrumb-item">
              <a href={this.url() + "/casemodels"}>Overview</a>
            </li>
            {this.CaseModelBreadCrump(casemodel)}
            {this.CaseBreadCrump(casemodel, caze)}
          </ol>
        </nav>
      </header>
    );
  }
}

export default NavBar;
