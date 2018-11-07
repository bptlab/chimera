import React, { Component } from "react";
import { getAvailableActivityOutput } from "../API";

class ChooseState extends Component {
  render() {
    const { dataclasses } = this.props;
    return (
      <div>
        {dataclasses.map((dataclass, idx) => (
          <div key={idx} className="input-group mb-3">
            <div className="input-group-prepend">
              <label className="input-group-text">
                {dataclass.dataclassName}
              </label>
            </div>
            <select className="custom-select">
              {dataclass.availableStates.map((state, idx) => (
                <option key={idx}>state</option>
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
    const { dataclass } = this.props;
    const { dataclassName, attributeConfiguration } = dataclass;
    return (
      <div>
        <label>{dataclassName}</label>
        {attributeConfiguration.map((attribute, idx) => (
          <div key={idx} className="input-group mb-3">
            <div className="input-group-prepend">
              <label className="input-group-text">{attribute.name}</label>
            </div>
            <input type="text" className="form-control" />
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
  state = {
    dataclasses: [],
    dataobjects: []
  };

  componentWillReceiveProps = async props => {
    const { id } = props.activity;
    if (id) {
      const { cmId, caseId } = props.match.params;
      const response = await getAvailableActivityOutput(cmId, caseId, id);
      console.log(response);
      const { dataclasses, dataobjects } = response;
      console.log(dataclasses);
      this.setState({ dataclasses, dataobjects });
    }
  };

  render() {
    const { activity, onSubmit } = this.props;
    const { dataclasses, dataobjects } = this.state;
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
                <label>Select states of data objects</label>
                <ChooseState dataclasses={dataclasses} />
                <hr />

                <label>Write values of data attributes</label>
                {dataclasses.map((dataclass, idx) => (
                  <div key={idx}>
                    <ChangeAttributeValues dataclass={dataclass} />
                    {idx === dataclasses.length - 1 ? "" : <hr />}
                  </div>
                ))}
              </div>
              <div className="modal-footer">
                <button type="button" className="btn" data-dismiss="modal">
                  Close
                </button>
                <button
                  type="button"
                  className="btn btn-default btn-primary"
                  onClick={() => onSubmit(activity, this.state.dataObjects)}
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
