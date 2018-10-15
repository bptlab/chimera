import React, { Component } from "react";
import { getAvailableActivityInput } from "../API";

class BeginActivityModal extends Component {
  state = {
    dataObjects: []
  };

  componentWillReceiveProps = async props => {
    const { id } = props.activity;
    if (id) {
      const { cmId, caseId } = props.match.params;
      const dataObjects = await getAvailableActivityInput(cmId, caseId, id);
      console.log(dataObjects);
      this.setState({ dataObjects });
    }
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
                  Start the activity
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
              </div>
              <div className="modal-footer">
                <button type="button" className="btn" data-dismiss="modal">
                  Close
                </button>
                <button
                  type="button"
                  className="btn btn-default btn-primary"
                  data-dismiss="modal"
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

export default BeginActivityModal;
