import React, { Component } from "react";
import { getAvailableActivityOutput } from "../API";

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
                  Terminate the activity
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
                <label>
                  Select data objects for '{activity.label}
                  '.
                </label>
                <br />
                <label>New Creation</label>
                {dataclasses.map(d => (
                  <div>
                    <h6>{d.dataclassName}</h6>
                    <label>Available States</label>
                    {d.availableStates.map(s => (
                      <div>
                        <label>{s}</label>
                      </div>
                    ))}
                    <label>Attributes</label>
                    {d.attributeConfiguration.map(a => (
                      <div>
                        <label>{a.name}</label>
                        <label>({a.type})</label>
                        <input
                          type="text"
                          value={a.value}
                          placeholder="Enter value"
                        />
                      </div>
                    ))}
                  </div>
                ))}
                <label>Transitions</label>
                {dataobjects.map(d => (
                  <div>
                    <h6>{d.dataclassName}</h6>
                    <label>Available States</label>
                    {d.availableStates.map(s => (
                      <div>
                        <label>{s}</label>
                      </div>
                    ))}
                    <label>Attributes</label>
                    {d.attributeConfiguration.map(a => (
                      <div>
                        <label>{a.name}</label>
                        <label>{a.type}</label>
                        <input
                          type="text"
                          value={a.value}
                          placeholder="Enter value"
                        />
                      </div>
                    ))}
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

export default TerminateActivityModal;
