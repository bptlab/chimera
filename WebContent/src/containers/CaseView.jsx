import React, { Component } from "react";
import PropTypes from "prop-types";

import {
  getCase,
  beginActivity,
  terminateActivity,
  closeCase,
  getAvailableActivityInput,
  getAvailableActivityOutput
} from "../API";
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
    dataObjectsForBegin: {},
    selectedActivityForTermination: { id: "", label: "" },
    terminationValues: {}
  };

  // output: {
  //   "activityId": [{
  //     "dataclassName": "",
  //     "availableStates": ["name"]
  //     "attributes": [{
  //       "name": "",
  //       "type": "",
  //       "value": ""
  //     }]
  //   }]
  // }
  // terminationValues: {
  //   "activityId": [{
  //     "dataclassName": "",
  //     "state": "", // muss hinzugefügt werden
  //     "availableStates": ["name"] // wird gelöscht
  //     "attributes": [{
  //       "name": ""
  //       "type": "" // wird gelöscht
  //       "value": ""
  //     }]
  //   }]
  // }
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

  selectActivityForBegin = async selectedActivityForBegin => {
    const { id } = selectedActivityForBegin;
    const { cmId, caseId } = this.props.match.params;
    const dataObjects = await getAvailableActivityInput(cmId, caseId, id);
    console.log(dataObjects);
    const dataObjectsForBegin = {
      ...this.dataObjectsForBegin,
      [id]: dataObjects
    };
    this.setState({ dataObjectsForBegin, selectedActivityForBegin });
  };

  selectActivityForTermination = async activity => {
    const { id } = activity;
    if (this.state.terminationValues[id]) {
      this.setState({
        selectedActivityForTermination: activity
      });
    } else {
      const { cmId, caseId } = this.props.match.params;
      const output = await getAvailableActivityOutput(cmId, caseId, id);

      const terminationValue = output.map(dataclass => ({
        ...dataclass,
        state: dataclass.availableStates[0]
      }));

      const terminationValues = {
        ...this.state.terminationValues,
        [id]: terminationValue
      };

      this.setState({
        selectedActivityForTermination: activity,
        terminationValues
      });
    }
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

  terminateActivityModal = () => {
    const {
      selectedActivityForTermination,
      terminationValues,
      dataclasses
    } = this.state;
    return (
      <TerminateActivityModal
        activity={selectedActivityForTermination}
        match={this.props.match}
        terminationValues={terminationValues[selectedActivityForTermination.id]}
        handleStateChanges={this.handleStateChanges}
        handleAttributeValueChanges={this.handleAttributeValueChanges}
        dataclasses={dataclasses}
        onSubmit={this.handleTerminateActivity}
      />
    );
  };

  handleStateChanges = (activity, dataclassName, state) => {
    const { id } = activity;
    let terminationValues = { ...this.state.terminationValues };
    let dataclass = terminationValues[id].find(
      d => d.dataclassName === dataclassName
    );
    dataclass.state = state;
    this.setState({ terminationValues });
  };

  handleAttributeValueChanges = (
    activity,
    dataclassName,
    attributeName,
    value
  ) => {
    const { id } = activity;

    let terminationValues = { ...this.state.terminationValues };
    let dataclass = terminationValues[id].find(
      d => d.dataclassName === dataclassName
    );

    let attribute = dataclass.attributes.find(a => a.name === attributeName);
    attribute.value = value;
    this.setState({ terminationValues });
  };

  // modify copy of state to fit API
  handleTerminateActivity = async activity => {
    const { cmId, caseId } = this.props.match.params;
    const { id } = activity;

    let activityTerminationValues = [...this.state.terminationValues[id]];
    activityTerminationValues.forEach(dataclass => {
      delete dataclass.availableStates;
      dataclass.attributes.forEach(attribute => {
        delete attribute.type;
      });
    });
    let terminationValues = { ...this.state.terminationValues };
    delete terminationValues[id];
    this.setState({ terminationValues });
    await terminateActivity(cmId, caseId, id, activityTerminationValues);
    this.componentDidMount();
  };

  // TODO: maybe pass not all matches but only the ids
  render() {
    const { caze, selectedActivityForBegin } = this.state;

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

          {this.terminateActivityModal()}

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
