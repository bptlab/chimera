import React, { Component } from "react";
import AttributeConfiguration from "../components/modals/AttributeConfiguration";

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
              disabled={dataclass.availableStates.length === 1}
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

class TerminateActivityModal extends Component {
  terminateActivityForm = () => {
    const { activity, terminationValues, handleStateChanges } = this.props;
    if (!terminationValues) {
      return "";
    }
    if (terminationValues.length === 0) {
      return <label>No output condition defined</label>;
    }
    return (
      <div>
        <label>Select states of data objects</label>
        <ChooseState
          activity={activity}
          terminationValues={terminationValues}
          handleStateChanges={handleStateChanges}
        />
        {this.dataAttributeChangesForm()}
      </div>
    );
  };

  dataAttributeChangesForm = () => {
    const {
      activity,
      terminationValues,
      handleAttributeValueChanges
    } = this.props;

    const dataclassWithAttributes = terminationValues.filter(
      dataclass => dataclass.attributes.length > 0
    );
    if (!dataclassWithAttributes) {
      return "";
    }

    const editDataAttributes = dataclass => {
      const { dataclassName, state, attributes } = dataclass;

      return (
        <React.Fragment>
          <label>
            {dataclassName}[{state}]
          </label>
          <AttributeConfiguration
            editable={true}
            attributes={attributes}
            handleAttributeValueChanges={(attributeName, newValue) =>
              handleAttributeValueChanges(
                activity,
                dataclassName,
                attributeName,
                newValue
              )
            }
          />
        </React.Fragment>
      );
    };

    return (
      <div>
        <hr />
        <label>Write values of data attributes</label>
        {dataclassWithAttributes.map((dataclass, idx) => (
          <div key={idx}>
            {editDataAttributes(dataclass)}
            {idx < dataclassWithAttributes.length - 1 ? <hr /> : ""}
          </div>
        ))}
      </div>
    );
  };

  render() {
    const { activity, onSubmit } = this.props;

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

              <div className="modal-body">{this.terminateActivityForm()}</div>
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
