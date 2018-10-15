import React, { Component } from "react";
import PropTypes from "prop-types";

import { getCase, beginActivity, closeCase } from "../API";
import NavBar from "./NavBar";
import BeginActivityModal from "../modals/BeginActivityModal";
import TerminateActivityModal from "../modals/TerminateActivityModal";

class CaseView extends Component {
  // TODO: return cmId
  state = {
    caze: {
      id: "",
      casemodel: {
        id: "",
        name: ""
      },
      name: "",
      terminated: false,
      canTerminate: false,
      instantiation: "",
      activities: {
        ready: [],
        running: []
      },
      dataObjects: []
    },
    selectedActivityForBegin: { id: "", label: "" },
    selectedActivityForTermination: { id: "", label: "" }
  };

  componentDidMount = async () => {
    const { cmId, caseId } = this.props.match.params;
    const caze = await getCase(cmId, caseId);
    this.setState({ caze });
  };

  handleBeginActivity = async (activity, dataObjects) => {
    const { cmId, caseId } = this.props.match.params;
    const { id } = activity;
    await beginActivity(cmId, caseId, id);
    this.componentDidMount();
  };

  selectActivityForBegin = activity => {
    this.setState({ selectedActivityForBegin: activity });
  };

  selectActivityForTermination = activity => {
    this.setState({ selectedActivityForTermination: activity });
  };

  closeCaseButton = () => {
    const { canTerminate } = this.state.caze;
    if (canTerminate) {
      return (
        <button className="btn btn-danger" onClick={this.handleCloseCase}>
          Close Case
        </button>
      );
    }
  };

  handleCloseCase = async () => {
    const { cmId, caseId } = this.props.match.params;
    await closeCase(cmId, caseId);
    this.componentDidMount();
  };

  // TODO: maybe pass not all matches but only the ids
  render() {
    const {
      caze,
      selectedActivityForBegin,
      selectedActivityForTermination
    } = this.state;
    return (
      <React.Fragment>
        <NavBar
          casemodel={{ id: this.props.match.params.cmId, name: "Case Model" }}
          caze={caze}
        />
        <main className="main-container">
          <BeginActivityModal
            activity={selectedActivityForBegin}
            match={this.props.match}
            onSubmit={this.handleBeginActivity}
          />
          <TerminateActivityModal
            activity={selectedActivityForTermination}
            match={this.props.match}
            onSubmit={this.handleTerminateActivity}
          />
          <div className="container">
            <h1>{caze.name}</h1>
            <h5>{caze.id}</h5>
            <h5>{caze.terminated ? "Terminated" : "Active"}</h5>
            {this.closeCaseButton()}
            <h5>Deployed on {caze.instantiation}</h5>
            <h5>Open activities</h5>
            {caze.activities.ready.map((a, idx) => (
              <button
                key={idx}
                className="btn"
                data-toggle="modal"
                data-target="#beginActivityModal"
                onClick={() => this.selectActivityForBegin(a)}
              >
                {a.label}
              </button>
            ))}
            <h5>Running activities</h5>
            {caze.activities.running.map((a, idx) => (
              <button
                key={idx}
                className="btn"
                data-toggle="modal"
                data-target="#terminateActivityModal"
                onClick={() => this.selectActivityForTermination(a)}
              >
                {a.label}
              </button>
            ))}
          </div>
        </main>
      </React.Fragment>
    );
  }
}

CaseView.propTypes = {
  caze: PropTypes.shape({
    name: PropTypes.string.isRequired,
    id: PropTypes.string.isRequired,
    instantiation: PropTypes.instanceOf(Date).isRequired,
    terminated: PropTypes.bool.isRequired,
    canTerminate: PropTypes.bool.isRequired
  })
};

export default CaseView;
