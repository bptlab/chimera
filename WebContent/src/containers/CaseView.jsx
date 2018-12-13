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
import BpmnFragment from "../components/BpmnFragment";
import DataObject from "../components/DataObject";
import ViewDataObjectModal from "../modals/ViewDataObjectModal";
import {
  ShowLogButton,
  AbortCaseButton,
  CloseCaseButton
} from "../components/case/Actions";

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
      dataobjects: []
    },
    selectedActivityForBegin: { id: "", label: "" },
    dataObjectsForBegin: {},
    selectedActivityForTermination: { id: "", label: "" },
    terminationValues: {},
    selectedDataObjectForView: {
      dataclass: "",
      state: "",
      locked: false,
      attributes: []
    }
  };

  componentDidMount = async () => {
    const { cmId, caseId } = this.props.match.params;
    let caze = await getCase(cmId, caseId);
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

  viewDataObjectModal = () => {
    const { selectedDataObjectForView } = this.state;
    return <ViewDataObjectModal dataObject={selectedDataObjectForView} />;
  };

  selectDataObjectForView = selectedDataObjectForView => {
    this.setState({ selectedDataObjectForView });
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

          {this.viewDataObjectModal()}

          <div className="container">
            <div className="row">
              <div className="col">
                <h1>{caze.name}</h1>
                <h5>Status: {caze.terminated ? "Terminated" : "Active"}</h5>
                <h5>Started: {caze.instantiation}</h5>
              </div>
              <div className="col-2">
                <div className="mb-2">
                  <ShowLogButton caze={caze} />
                </div>
                <div className="mb-2">
                  <AbortCaseButton caze={caze} />
                </div>
                <div>
                  <CloseCaseButton
                    caze={caze}
                    handleCloseCase={this.handleCloseCase}
                  />
                </div>
              </div>
            </div>
            <hr />
            <h5>Data Objects</h5>

            {caze.dataobjects.length > 0 ? (
              <div className="row">
                {caze.dataobjects.map((d, idx) => (
                  <div
                    key={idx}
                    style={{ display: "inline-block" }}
                    className="ml-2"
                  >
                    <DataObject
                      dataObject={d}
                      handleClick={this.selectDataObjectForView}
                    />
                  </div>
                ))}
              </div>
            ) : (
              <p>No existing Data Objects</p>
            )}
            <hr />
            <div className="row">
              <div className="col-4">
                <h5>Open activities</h5>
                <div className="">
                  {caze.activities.ready.map((a, idx) => (
                    <div key={idx} className="mb-2">
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
                </div>
              </div>
              <div className="col-4">
                <h5>Running activities</h5>
                <div className="">
                  {caze.activities.running.map((a, idx) => (
                    <div key={idx} className="mb-2">
                      <button
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
              </div>
            </div>
            <hr />
            {caze.casemodel.fragments.map((f, idx) => (
              <BpmnFragment
                style={{ height: 1000 }}
                key={idx}
                id={"bpmnViewer_" + idx}
                xml={f.bpmn}
              />
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
