import React, { Component } from "react";
import PropTypes from "prop-types";

import {
  getCase,
  beginActivity,
  terminateActivity,
  closeCase,
  getAvailableActivityInput,
  getAvailableActivityOutput,
  getFragments
} from "../API";
import NavBar from "./NavBar";
import BeginActivityModal from "../modals/BeginActivityModal";
import TerminateActivityModal from "../modals/TerminateActivityModal";
import BpmnFragment from "../components/BpmnFragment";

class CaseView extends Component {
  // TODO: return cmId
  state = {
    caze: {
      id: "",
      casemodel: {
        id: "",
        name: "",
        fragments: []
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

  componentDidMount = async () => {
    const { cmId, caseId } = this.props.match.params;
    let caze = await getCase(cmId, caseId);
    const fragments = await getFragments(cmId);
    caze.casemodel = { fragments };
    this.setState({ caze });
  };

  handleBeginActivity = async activity => {
    const { id } = activity;
    const { cmId, caseId } = this.props.match.params;
    const { dataObjectsForBegin } = this.state;

    const selectedIds = Object.keys(dataObjectsForBegin).reduce(
      (selectedIds, dataclass) => {
        const { id } = dataObjectsForBegin[dataclass].selected;
        selectedIds.push(id);
        return selectedIds;
      },
      []
    );
    await beginActivity(cmId, caseId, id, selectedIds);
    this.componentDidMount();
  };

  selectActivityForBegin = async selectedActivityForBegin => {
    const { id } = selectedActivityForBegin;
    const { cmId, caseId } = this.props.match.params;
    let dataObjects = await getAvailableActivityInput(cmId, caseId, id);

    const dataObjectsForBegin = dataObjects.reduce((map, obj) => {
      const dataclassName = obj.dataclass;
      if (!map[dataclassName]) {
        map[dataclassName] = { dataobjects: [], selected: obj };
      }
      map[obj.dataclass].dataobjects.push(obj);
      return map;
    }, {});
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

  beginActivityModal = () => {
    const { selectedActivityForBegin, dataObjectsForBegin } = this.state;
    return (
      <BeginActivityModal
        activity={selectedActivityForBegin}
        beginValues={dataObjectsForBegin}
        onSubmit={this.handleBeginActivity}
        onDataObjectChanges={this.handleDataObjectChanges}
      />
    );
  };

  handleDataObjectChanges = (dataclassName, dataObjectId) => {
    const dataclass = this.state.dataObjectsForBegin[dataclassName];
    const selected = dataclass.dataobjects.find(d => d.id === dataObjectId);

    const updatedDataclass = {
      ...dataclass,
      selected
    };
    const dataObjectsForBegin = {
      ...this.state.dataObjectsForBegin,
      [dataclassName]: updatedDataclass
    };
    this.setState({ dataObjectsForBegin });
  };

  terminateActivityModal = () => {
    const { selectedActivityForTermination, terminationValues } = this.state;
    return (
      <TerminateActivityModal
        activity={selectedActivityForTermination}
        terminationValues={terminationValues[selectedActivityForTermination.id]}
        handleStateChanges={this.handleStateChanges}
        handleAttributeValueChanges={this.handleAttributeValueChanges}
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

  render() {
    const { caze } = this.state;

    return (
      <React.Fragment>
        <NavBar
          casemodel={{ id: this.props.match.params.cmId, name: "Case Model" }}
          caze={caze}
        />
        <main className="main-container">
          {this.beginActivityModal()}

          {this.terminateActivityModal()}

          <div className="container">
            <h1>{caze.name}</h1>
            <h5>{caze.id}</h5>
            <h5>{caze.terminated ? "Terminated" : "Active"}</h5>
            {this.closeCaseButton()}
            <h5>Deployed on {caze.instantiation}</h5>
            <h5>Open activities</h5>
            {caze.activities.ready.map((a, idx) => (
              <div key={idx}>
                <button
                  className="btn"
                  data-toggle="modal"
                  data-target="#beginActivityModal"
                  onClick={() => this.selectActivityForBegin(a)}
                >
                  {a.label}
                </button>
              </div>
            ))}
            <h5>Running activities</h5>
            {caze.activities.running.map((a, idx) => (
              <div>
                <button
                  key={idx}
                  className="btn"
                  data-toggle="modal"
                  data-target="#terminateActivityModal"
                  onClick={() => this.selectActivityForTermination(a)}
                >
                  {a.label}
                </button>
              </div>
            ))}
          </div>
          {caze.casemodel.fragments.map((xml, idx) => (
            <BpmnFragment
              style={{ height: 1000 }}
              key={idx}
              id={"bpmnViewer_" + idx}
              xml={xml}
            />
          ))}
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
