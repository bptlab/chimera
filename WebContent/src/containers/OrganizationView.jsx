import React, { Component } from "react";
import { Link } from "react-router-dom";
import PropTypes from "prop-types";

import { getCaseModels } from "../API";
import NavBar from "./NavBar";
import ExpandableOverview from "../components/ExpandableOverview";

class OrganizationView extends Component {
  state = {
    casemodels: []
  };

  componentDidMount = async () => {
    let casemodels = await getCaseModels();
    // per default all casemodels are collapsed
    casemodels.map(cm => (cm.isCollapsed = false));
    this.setState({ casemodels });
  };

  render() {
    return (
      <React.Fragment>
        <NavBar />
        <main className="main-container">
          <div className="container">
            {this.state.casemodels.map((cm, idx) => {
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
