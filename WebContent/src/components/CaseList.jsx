import React, { Component } from "react";
import { Link } from "react-router-dom";

import ExpandableOverview from "../components/ExpandableOverview";
import ListArrangeOptions from "../components/ArrangeLists";
import { timeDiff } from "../helper/DateMethods";

class CaseList extends Component {
  state = {
    cmId: "",
    cases: [],
    displayedCases: [],
    filter: "",
    sortingOptions: ["Newest", "Oldest"],
    sorting: "Newest"
  };

  sortCases = casemodels => {
    const { sorting } = this.state;
    switch (sorting) {
      case "Newest":
        casemodels.sort(function(a, b) {
          return b.instantiation - a.instantiation;
        });
        break;
      case "Oldest":
        casemodels.sort(function(a, b) {
          return a.instantiation - b.instantiation;
        });
        break;
      default:
        break;
    }
  };

  componentWillReceiveProps(props) {
    const { cmId, cases } = props;
    this.setState({ cmId, cases }, () => this.handleOptionsChange());
  }

  handleSortChange = sorting => {
    this.setState({ sorting }, () => this.handleOptionsChange());
  };

  handleFilterChange = filter => {
    this.setState({ filter }, () => this.handleOptionsChange());
  };

  handleOptionsChange = () => {
    const { cases, filter } = this.state;
    const filteredCases = cases.filter(casemodel =>
      casemodel.name.includes(filter)
    );
    this.sortCases(filteredCases);
    this.setState({
      displayedCases: filteredCases
    });
  };

  render() {
    const { cmId, displayedCases } = this.state;
    return (
      <React.Fragment>
        <ListArrangeOptions
          sortingOptions={this.state.sortingOptions}
          onSortingChange={this.handleSortChange}
          onFilterChange={this.handleFilterChange}
        />
        {displayedCases.map((c, idx) => {
          const caseOverviewHeader = (
            <div>
              <Link to={cmId + "/cases/" + c.id}>{c.name}</Link>
              &nbsp;- {timeDiff(c.instantiation)} ago
            </div>
          );
          return (
            <ExpandableOverview key={idx} idx={idx} header={caseOverviewHeader}>
              <p>Status: {c.terminated ? "Terminated" : "Running"}</p>

              <p>
                Termination:{" "}
                {c.canTerminate ? "can terminate" : "cannot terminate"}
              </p>
              <p>Instantiation: {c.instantiation.toLocaleString()}</p>
            </ExpandableOverview>
          );
        })}
      </React.Fragment>
    );
  }
}

export default CaseList;
