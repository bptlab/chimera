import React, { Component } from "react";
import { Link } from "react-router-dom";
import PropTypes from "prop-types";

import { getCaseModels } from "../API";
import NavBar from "./NavBar";
import ExpandableOverview from "../components/ExpandableOverview";
import SearchField from "../components/SearchField"

class OrganizationView extends Component {
  state = {
    casemodels: [],
    displayedCasemodels : [],
  };

  componentDidMount = async () => {
    let casemodels = await getCaseModels();
    // per default all casemodels are collapsed
    casemodels.map(cm => (cm.isCollapsed = false));
    this.setState({
      casemodels: casemodels,
      displayedCasemodels: casemodels
    });
  };

  filterCasemodels(filterString){
    let filteredCasemodels = this.state.casemodels.filter(casemodel => casemodel.name.includes(filterString));
    this.setState({
      casemodels: this.state.casemodels,
      displayedCasemodels: filteredCasemodels,})
  }

  render() {
    return (
      <React.Fragment>
        <NavBar />
        <SearchField searchHandler={filterString => this.filterCasemodels(filterString)}/>
        <main className="main-container">
          <div className="container">
            {this.state.displayedCasemodels.map((cm, idx) => {
              const casemodelOverviewHeader = (
                <Link to={"casemodels/" + cm.id}>{cm.name}</Link>
              );
              return (
                <ExpandableOverview
                  key={idx}
                  cm={cm}
                  idx={idx}
                  header={casemodelOverviewHeader}
                  body={"cm.description"}
                />
              );
            })}
          </div>
        </main>
      </React.Fragment>
    );
  }
}

OrganizationView.propTypes = {
  casemodels: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string.isRequired,
      id: PropTypes.string.isRequired,
      isCollapsed: PropTypes.bool.isRequired
    })
  )
};

export default OrganizationView;
