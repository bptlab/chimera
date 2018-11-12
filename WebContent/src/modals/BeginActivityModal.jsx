import React, { Component } from "react";
import AttributeConfiguration from "../components/modals/AttributeConfiguration";

class ChooseDataObject extends Component {
  render() {
    const {
      dataclassName,
      dataobjects,
      selected,
      onDataObjectChanges
    } = this.props;

    return (
      <div className="input-group mb-3">
        <div className="input-group-prepend">
          <label className="input-group-text">{dataclassName}</label>
        </div>
        <select
          className="custom-select"
          defaultValue={selected.id}
          onChange={event => onDataObjectChanges(event.target.value)}
          disabled={dataobjects.length === 1}
        >
          {dataobjects.map((dataObject, idx) => (
            <option key={idx}>{dataObject.id}</option>
          ))}
        </select>
      </div>
    );
  }
}

class BeginActivityModal extends Component {
  selectDataObjectForm = () => {
    const { beginValues, onDataObjectChanges } = this.props;
    if (
      Object.keys(beginValues).length === 0 &&
      beginValues.constructor === Object
    ) {
      return <label>No input condition defined</label>;
    }
    return (
      <React.Fragment>
        <label>Select data objects for each data class</label>
        {Object.keys(beginValues).map((dataclassName, idx) => {
          return (
            <ChooseDataObject
              key={idx}
              dataclassName={dataclassName}
              dataobjects={beginValues[dataclassName].dataobjects}
              selected={beginValues[dataclassName].selected}
              onDataObjectChanges={dataObjectId =>
                onDataObjectChanges(dataclassName, dataObjectId)
              }
            />
          );
        })}
        <hr />

        {Object.keys(beginValues).map((dataclassName, idx) => {
          return (
            <div key={idx}>
              <label>
                {beginValues[dataclassName].selected.dataclass}[
                {beginValues[dataclassName].selected.state}]
              </label>
              <AttributeConfiguration
                editable={false}
                attributes={beginValues[dataclassName].selected.attributes}
              />
            </div>
          );
        })}
      </React.Fragment>
    );
  };

  render() {
    const { activity, onSubmit } = this.props;

    return (
      <div
        className="modal fade bs-example-modal-sm"
        tabIndex="-1"
        role="dialog"
        aria-labelledby="beginActivityModalTitle"
        id="beginActivityModal"
      >
        <div className="modal-dialog">
          <div className="modal-content">
            <form>
              <div className="modal-header">
                <h4 className="modal-title" id="beginActivityModalTitle">
                  Start the activity: {activity.label}
                </h4>
                <button
                  type="button"
                  className="close"
                  data-dismiss="modal"
                  aria-label="Close"
                >
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div className="modal-body">{this.selectDataObjectForm()}</div>
              <div className="modal-footer">
                <button type="button" className="btn" data-dismiss="modal">
                  Close
                </button>
                <button
                  type="button"
                  className="btn btn-default btn-primary"
                  data-dismiss="modal"
                  onClick={() => onSubmit(activity)}
                >
                  Start
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }
}

export default BeginActivityModal;
