import React, { Component } from "react";
import { Link } from "react-router-dom";
import PropTypes from "prop-types";

import ExpandableOverview from "../components/ExpandableOverview";
import ListArrangeOptions from "../components/ArrangeLists";
import { timeDiff } from "../helper/DateMethods";
class CaseModelList extends Component {
  state = {
    casemodels: [],
    displayedCasemodels: [],
    filter: "",
    sortingOptions: ["Newest", "Oldest"],
    sorting: "Newest"
  };

  sortCasemodels = casemodels => {
    const { sorting } = this.state;
    switch (sorting) {
      case "Newest":
        casemodels.sort(function(a, b) {
          return b.deployment - a.deployment;
        });
        break;
      case "Oldest":
        casemodels.sort(function(a, b) {
          return a.deployment - b.deployment;
        });
        break;
      default:
        break;
    }
  };

  componentWillReceiveProps(props) {
    const { casemodels } = props;
    this.setState({ casemodels }, () => this.handleOptionsChange());
  }

  handleSortChange = sorting => {
    this.setState({ sorting }, () => this.handleOptionsChange());
  };

  handleFilterChange = filter => {
    this.setState({ filter }, () => this.handleOptionsChange());
  };

  handleOptionsChange = () => {
    const { casemodels, filter } = this.state;
    const filteredCasemodels = casemodels.filter(casemodel =>
      casemodel.name.includes(filter)
    );
    this.sortCasemodels(filteredCasemodels);
    this.setState({
      displayedCasemodels: filteredCasemodels
    });
  };

  render() {
    return (
      <React.Fragment>
        <main className="main-container">
          <div className="container">
            <ListArrangeOptions
              sortingOptions={this.state.sortingOptions}
              onSortingChange={this.handleSortChange}
              onFilterChange={this.handleFilterChange}
            />
            {this.state.displayedCasemodels.map((cm, idx) => {
              const casemodelOverviewHeader = (
                <div>
                  <Link to={"casemodels/" + cm.id}>
                    {cm.name} ({cm.modelversion})
                  </Link>
                  &nbsp;- {timeDiff(cm.deployment)} ago
                </div>
              );
              return (
                <ExpandableOverview
                  key={idx}
                  cm={cm}
                  idx={idx}
                  header={casemodelOverviewHeader}
                >
                  {cm.description ? (
                    <p style={{ wordWrap: "break-word" }}>
                      Description: {cm.description}
                    </p>
                  ) : null}
                  {cm.deployment.toLocaleString()}
                </ExpandableOverview>
              );
            })}
          </div>
        </main>
      </React.Fragment>
    );
  }
}

CaseModelList.propTypes = {
  casemodels: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string.isRequired,
      id: PropTypes.string.isRequired
    })
  )
};

export default CaseModelList;
