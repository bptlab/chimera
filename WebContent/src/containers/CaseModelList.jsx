import React, { Component } from "react";
import { Link } from "react-router-dom";
import PropTypes from "prop-types";

import { getCaseModels } from "../API";
import ExpandableOverview from "../components/ExpandableOverview";
import SearchField from "../components/SearchField"

class CaseModelList extends Component {
  state = {
    casemodels: [],
    displayedCasemodels : [],
    filterString: "",
  };

  filterCasemodels(casemodels, filterString){
    let filteredCasemodels = casemodels.filter(casemodel => casemodel.name.includes(filterString));
    this.setState({
      displayedCasemodels: filteredCasemodels,
      filterString: filterString,
    });
  }

  componentWillReceiveProps(nextProps){
    this.filterCasemodels(nextProps.casemodels, this.state.filterString);
  }

  render() {
    return (
      <React.Fragment>
        <SearchField searchHandler={filterString => {
          this.filterCasemodels(this.props.casemodels, filterString);
        }}/>
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

CaseModelList.propTypes = {
  casemodels: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string.isRequired,
      id: PropTypes.string.isRequired
    })
  )
};

export default CaseModelList;
