import React, { Component } from "react";

class ChooseState extends Component {
  render() {
    const { activity, terminationValues, handleStateChanges } = this.props;

    return (
      <div>
        {terminationValues.map((dataclass, idx) => (
          <div key={idx} className="input-group mb-3">
            <div className="input-group-prepend">
              <label className="input-group-text">
                {dataclass.dataclassName}
              </label>
            </div>
            <select
              className="custom-select"
              defaultValue={terminationValues.state}
              onChange={event =>
                handleStateChanges(
                  activity,
                  dataclass.dataclassName,
                  event.target.value
                )
              }
            >
              {dataclass.availableStates.map((state, idx) => (
                <option key={idx}>{state}</option>
              ))}
            </select>
          </div>
        ))}
      </div>
    );
  }
}

class ChangeAttributeValues extends Component {
  render() {
    const { activity, dataclass, handleAttributeValueChanges } = this.props;
    const { dataclassName, attributes } = dataclass;
    return (
      <div>
        <label>{dataclassName}</label>
        {attributes.map((attribute, idx) => (
          <div key={idx} className="input-group mb-3">
            <div className="input-group-prepend">
              <label className="input-group-text">{attribute.name}</label>
            </div>
            <input
              type="text"
              className="form-control"
              value={attribute.value || ""}
              onChange={event =>
                handleAttributeValueChanges(
                  activity,
                  dataclassName,
                  attribute.name,
                  event.target.value
                )
              }
            />
            <div className="input-group-append">
              <label className="input-group-text">{attribute.type}</label>
            </div>
          </div>
        ))}
      </div>
    );
  }
}

class TerminateActivityModal extends Component {
  dataAttributeChangesForm = () => {
    const {
      activity,
      terminationValues,
      handleAttributeValueChanges
    } = this.props;
    if (!terminationValues) {
      return "";
    }

    return (
      <div>
        <label>Write values of data attributes</label>
        {terminationValues.map((dataclass, idx) => (
          <div key={idx}>
            <ChangeAttributeValues
              activity={activity}
              dataclass={dataclass}
              handleAttributeValueChanges={handleAttributeValueChanges}
            />
            {idx === terminationValues.length - 1 ? "" : <hr />}
          </div>
        ))}
      </div>
    );
  };
  render() {
    const {
      activity,
      onSubmit,
      terminationValues,
      handleStateChanges
    } = this.props;

    // const { dataclasses, dataobjects } = this.state;
    return (
      <div
        className="modal fade bs-example-modal-sm"
        tabIndex="-1"
        role="dialog"
        aria-labelledby="terminateActivityModalTitle"
        id="terminateActivityModal"
      >
        <div className="modal-dialog">
          <div className="modal-content">
            <form>
              <div className="modal-header">
                <h4 className="modal-title" id="terminateActivityModalTitle">
                  Complete the Task: {activity.label}
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
              <div className="modal-body">
                {terminationValues ? (
                  <div>
                    <label>Select states of data objects</label>
                    <ChooseState
                      activity={activity}
                      terminationValues={terminationValues}
                      handleStateChanges={handleStateChanges}
                    />
                  </div>
                ) : (
                  ""
                )}
                <hr />

                {this.dataAttributeChangesForm()}
              </div>
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
                  Complete
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }
}

export default TerminateActivityModal;
